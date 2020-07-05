package com.example.books.app

import android.app.Application
import com.example.books.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class BooksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BooksApplication)
            modules(listOf(viewModelModule, repositoryModule, apiModule, netModule, databaseModule, glideModule))
        }
    }
}