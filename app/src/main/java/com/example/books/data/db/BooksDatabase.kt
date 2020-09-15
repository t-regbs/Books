package com.example.books.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.books.data.model.Book
import com.example.books.data.model.RemoteKeys

@Database(entities = [Book::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract val bookDao: BooksDao
    abstract val remoteKeysDao: RemoteKeysDao
}