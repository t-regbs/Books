package com.example.books

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.books.api.BookService
import com.example.books.db.BooksDatabase
import com.example.books.db.BooksLocalCache
import com.example.books.repository.BooksRepository
import com.example.books.ui.ViewModelFactory
import java.util.concurrent.Executors

object Injection {

    /**
     * Creates an instance of [BooksLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): BooksLocalCache {
        val database = BooksDatabase.getInstance(context)
        return BooksLocalCache(database.bookDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [BooksRepository] based on the [BookService] and a
     * [BooksLocalCache]
     */
    private fun provideBooksRepository(context: Context): BooksRepository {
        return BooksRepository(BookService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideBooksRepository(context))
    }
}