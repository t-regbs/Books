package com.example.books.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.books.data.api.*
import com.example.books.data.db.BooksDatabase
import com.example.books.data.model.Book
import com.example.books.data.model.RemoteKeys
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException

private const val BOOKS_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class BooksRemoteMediator(
    private val title: String?,
    private val author: String?,
    private val publisher: String?,
    private val isbn: String?,
    private val key: String,
    private val service: BookService,
    private val booksDatabase: BooksDatabase
) : RemoteMediator<Int, Book>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Book>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                Timber.d("LoadType = REFRESH")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: BOOKS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Timber.d("PREPEND")
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
//                val remoteKey = getRemoteKeyForFirstItem(state)
//                    ?: throw InvalidObjectException("Something went wrong.")
//                remoteKey.prevKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = true)
//                remoteKey.prevKey
            }
            LoadType.APPEND -> {
                Timber.d("APPEND")
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        Timber.d("title: $title, page: $page")
        val sb = StringBuilder()
        if (!title.isNullOrBlank()) sb.append("$TITLE$title+")
        if (!author.isNullOrBlank()) sb.append("$AUTHOR$author+")
        if (!publisher.isNullOrBlank()) sb.append("$PUBLISHER$publisher+")
        if (!isbn.isNullOrBlank()) sb.append("$ISBN$isbn+")
        sb.setLength(sb.length - 1)
        val apiQuery = sb.toString()

        try {
            val apiResponse = service.searchBooks(apiQuery, key, state.config.pageSize, page)

            val books = apiResponse.items
            val endOfPaginationReached = books.isEmpty()
            booksDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    booksDatabase.remoteKeysDao.clearRemoteKeys()
                    booksDatabase.bookDao.clearBooks()
                }
                val prevKey = if (page == BOOKS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                Timber.d("prevKey = $prevKey, nextKey = $nextKey")
                val keys = books.map {
                    RemoteKeys(bookId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                booksDatabase.bookDao.insert(books)
                booksDatabase.remoteKeysDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Book>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                // Get the remote keys of the last item retrieved
                booksDatabase.remoteKeysDao.remoteKeysBookId(book.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Book>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { book ->
                // Get the remote keys of the first items retrieved
                booksDatabase.remoteKeysDao.remoteKeysBookId(book.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Book>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { bookId ->
                booksDatabase.remoteKeysDao.remoteKeysBookId(bookId)
            }
        }
    }

}