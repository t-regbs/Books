package com.example.books.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.books.BuildConfig
import com.example.books.data.api.BookService
import com.example.books.data.db.BooksDatabase
import com.example.books.data.model.Book
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

private const val apiKey: String = BuildConfig.API_KEY

class BooksRepository(private val service: BookService, private val database: BooksDatabase) {
    companion object {
        private const val NETWORK_PAGE_SIZE = 40
    }

    fun getSearchResultStream(
        title: String = "",
        author: String = "",
        publisher: String = "",
        isbn: String = ""
    ): Flow<PagingData<Book>> {
        Timber.d("new search: $title")
        val dbQuery = "%${title.replace(' ', '%')}%"
        val pagingSourceFactory =  { database.bookDao.getBooks(dbQuery, author, publisher)}

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = BooksRemoteMediator(title, author, publisher, isbn, apiKey, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}