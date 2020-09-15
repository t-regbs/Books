package com.example.books.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.books.data.model.Book

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(books: List<Book>)

    @Query("DELETE FROM books")
    suspend fun clearBooks()

    @Query("SELECT * FROM books WHERE (title LIKE :title) OR (authors LIKE :author) " +
            "OR (publisher LIKE :publisher) ORDER BY title ASC")
    fun getBooks(title: String, author: String = "", publisher: String = ""): PagingSource<Int, Book>
}