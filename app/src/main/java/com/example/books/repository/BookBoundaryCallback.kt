package com.example.books.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.books.util.Injection
import com.example.books.api.BookService
import com.example.books.api.searchBooks
import com.example.books.db.BooksDao
import com.example.books.model.Book
import kotlinx.coroutines.*
import timber.log.Timber

class BookBoundaryCallback(
        private val title: String = "", private val author: String = "",
        private val publisher: String = "", private val service: BookService,
        private val booksDao: BooksDao,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Default)
    : PagedList.BoundaryCallback<Book>() {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 0

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    companion object {
        private const val NETWORK_PAGE_SIZE = 40
    }

    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded called")
        val scope = CoroutineScope(dispatcher)
        scope.launch{
            requestAndSaveData(title, author, publisher)
        }

    }

    override fun onItemAtEndLoaded(itemAtEnd: Book) {
        Timber.d("onItemAtEndLoaded called")
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            requestAndSaveData(title, author, publisher)
        }

    }

    private suspend fun requestAndSaveData(query: String, author: String, publisher: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        _loadingProgress.postValue(true)
        searchBooks(
                service, query, author, publisher, "", NETWORK_PAGE_SIZE,
                Injection.API_KEY, lastRequestedPage, { books ->
            booksDao.insert(books)
            lastRequestedPage++
            isRequestInProgress = false
            _loadingProgress.postValue(false)
        }, {error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
            _loadingProgress.postValue(false)

        })
    }
}