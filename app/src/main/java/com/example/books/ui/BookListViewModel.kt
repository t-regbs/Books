package com.example.books.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.books.model.Book
import com.example.books.model.BookSearchResult
import com.example.books.repository.BooksRepository

class BookListViewModel(private val repository: BooksRepository) : ViewModel() {

    private var title = ""
    private var author = ""
    private var publisher = ""
    private var isbn = ""
    private val queryLiveData = MutableLiveData<List<String?>>()
    private val bookResult: LiveData<BookSearchResult> = Transformations.map(queryLiveData) { query ->
        title = query[0] ?: ""
        if (query.size > 1) {
            author = query[1] ?: ""
            publisher = query[2] ?: ""
            isbn = query[3] ?: ""
        } else {
            author = ""
            publisher = ""
            isbn = ""
        }
        repository.search(title, author, publisher, isbn)
    }

    val books: LiveData<PagedList<Book>> = Transformations.switchMap(bookResult) { it.data}
    val networkErrors: LiveData<String> = Transformations.switchMap(bookResult) { it.networkErrors }

    fun searchBooks(query: List<String?>) {
        queryLiveData.postValue(query)
    }

    fun lastTitleValue(): String? = queryLiveData.value?.get(0)
}