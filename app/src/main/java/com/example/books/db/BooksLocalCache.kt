package com.example.books.db

import android.util.Log
import androidx.paging.DataSource
import com.example.books.model.Book
import java.util.concurrent.Executor

class BooksLocalCache(private val booksDao: BooksDao, private val ioExecutor: Executor) {
    fun insert(books: List<Book>, insertFinished: () -> Unit) {
        Log.d("BooksLocalCache", "inserting ${books.size} books")
        booksDao.insert(books)
        insertFinished()
    }

    fun getBooks(title: String, author: String = "", publisher: String = "", isbn: String = ""): DataSource.Factory<Int, Book> {
        return booksDao.getBooks(title, author, publisher, isbn)
    }
}