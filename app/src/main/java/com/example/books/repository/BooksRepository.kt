package com.example.books.repository

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.example.books.api.BookService
import com.example.books.db.BooksLocalCache
import com.example.books.model.BookSearchResult

class BooksRepository(private val service: BookService, private val cache: BooksLocalCache) {
    fun search(title: String, author: String = "", publisher: String = "", isbn: String = "")
            : BookSearchResult {
        Log.d("BookRepository", "new search: $title")

        val dataSourceFactory = cache.getBooks(title, author, publisher, isbn)

        val boundaryCallback = BookBoundaryCallback(title, author, publisher, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        return BookSearchResult(data, networkErrors)

    }
    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}