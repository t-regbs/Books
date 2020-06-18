package com.example.books.repository

import androidx.paging.LivePagedListBuilder
import com.example.books.api.BookService
import com.example.books.db.BooksDao
import com.example.books.model.BookSearchResult
import timber.log.Timber

class BooksRepository(private val service: BookService, private val booksDao: BooksDao) {
    fun search(title: String = "", author: String = "", publisher: String = "", isbn: String = "")
            : BookSearchResult {
        Timber.d("new search: $title")

        val query = "%${title.replace(' ', '%')}%"
        val dataSourceFactory = booksDao.getBooks(query, author, publisher)

        val boundaryCallback = BookBoundaryCallback(title, author, publisher, service, booksDao)
        val networkErrors = boundaryCallback.networkErrors
        val  loadingProgress = boundaryCallback.loadingProgress

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        return BookSearchResult(data, networkErrors, loadingProgress)

    }
    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}