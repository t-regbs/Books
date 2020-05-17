package com.example.books.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.books.Injection
import com.example.books.api.BookService
import com.example.books.api.searchBooks
import com.example.books.db.BooksLocalCache
import com.example.books.model.Book

class BookBoundaryCallback(
        private val title: String = "", private val author: String = "",
        private val publisher: String = "", private val service: BookService,
        private val cache: BooksLocalCache)
    : PagedList.BoundaryCallback<Book>() {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    companion object {
        private const val NETWORK_PAGE_SIZE = 40
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData(title, author, publisher)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Book) {
        requestAndSaveData(title, author, publisher)
    }

    private fun requestAndSaveData(query: String, author: String, publisher: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchBooks(
                service, query, author = author, publisher = publisher, max = NETWORK_PAGE_SIZE,
                key = Injection.API_KEY, isbn = "",
                onSuccess = { books ->
                    cache.insert(books) {
                        lastRequestedPage++
                        isRequestInProgress = false
                    }
                },
                onError = { error ->
                    _networkErrors.postValue(error)
                    isRequestInProgress = false
                }
        )
    }
}