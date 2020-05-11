package com.example.books.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.books.DataConverter
import com.example.books.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun bookDao(): BooksDao

    companion object {
        @Volatile
        private var INSTANCE: BooksDatabase? = null

        fun getInstance(context: Context): BooksDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        BooksDatabase::class.java, "Books.db")
                        .build()
    }
}