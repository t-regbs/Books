package com.example.books.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.books.repository.BooksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Suppress("UNCHECKED_CAST")
class ViewModelFactory (
        private val booksRepository: BooksRepository
) : ViewModelProvider.NewInstanceFactory() {
    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (BookListViewModel(booksRepository) as T)
}