package com.example.books.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.books.repository.BooksRepository

class ViewModelFactory(private val repository: BooksRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}