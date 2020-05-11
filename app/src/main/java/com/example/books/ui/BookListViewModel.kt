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

    private val titleLiveData = MutableLiveData<String>()
    private val bookResult: LiveData<BookSearchResult> = Transformations.map(titleLiveData) { title ->
        repository.search(title)
    }

    val books: LiveData<PagedList<Book>> = Transformations.switchMap(bookResult) { it.data}
    val networkErrors: LiveData<String> = Transformations.switchMap(bookResult) { it.networkErrors }

    fun searchBooks(title: String) {
        titleLiveData.postValue(title)
    }

    fun lastTitleValue(): String? = titleLiveData.value
}