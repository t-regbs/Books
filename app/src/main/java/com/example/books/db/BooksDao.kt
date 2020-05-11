package com.example.books.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.books.model.Book

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(books: List<Book>)

    @Query("SELECT * FROM books WHERE (title LIKE :title) OR (authors LIKE :author) " +
            "OR (publisher LIKE :publisher) ORDER BY title ASC")
    fun getBooks(title: String, author: String = "", publisher: String = "", isbn: String = "")
            : DataSource.Factory<Int, Book>
}