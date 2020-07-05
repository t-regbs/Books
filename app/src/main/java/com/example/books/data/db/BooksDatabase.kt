package com.example.books.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.books.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract val bookDao: BooksDao
}