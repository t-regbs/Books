package com.example.books.db

import android.util.Log
import androidx.paging.DataSource
import com.example.books.model.Book
import timber.log.Timber
import java.util.concurrent.Executor

class BooksLocalCache(private val booksDao: BooksDao, private val ioExecutor: Executor) {

    fun insert(books: List<Book>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Timber.d("inserting ${books.size} books")
            Timber.d( "inserting ${books[0]} ")
            booksDao.insert(books)
            insertFinished()
        }
    }

    fun getBooks(title: String, author: String = "", publisher: String = "", isbn: String = ""): DataSource.Factory<Int, Book> {
        val query = "%${title.replace(' ', '%')}%"
        return booksDao.getBooks(query, author, publisher)
    }
}