package com.example.books

import android.app.Application
import com.example.books.repository.BooksRepository
import timber.log.Timber

class BooksApplication : Application() {
    val booksRepository: BooksRepository
        get() = Injection.provideBooksRepository(this)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}