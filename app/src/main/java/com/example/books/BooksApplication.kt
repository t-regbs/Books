package com.example.books

import android.app.Application
import timber.log.Timber

class BooksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}