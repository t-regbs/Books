package com.example.books.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.books.R
import com.example.books.api.BookService
import com.example.books.db.BooksDatabase
import com.example.books.repository.BooksRepository

object Injection {
    const val API_KEY = "AIzaSyBxHmT9nFCp9n2uOHkS3Gcq2OO3zbxaMrw"

    private var database: BooksDatabase? = null

    @Volatile
    var booksRepository: BooksRepository? = null

    private fun createBooksRepository(context: Context):BooksRepository{
        val database = database ?: BooksDatabase.getInstance(context)
        val newRepo = BooksRepository(BookService.create(), database.bookDao())
        booksRepository = newRepo
        return newRepo
    }

    /**
     * Creates an instance of [BooksRepository] based on the [BookService]
     */
    fun provideBooksRepository(context: Context): BooksRepository{
        synchronized(this){
            return booksRepository
                    ?: createBooksRepository(context)
        }
    }


    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
                .placeholderOf(R.drawable.book_open)
                .error(R.drawable.book_open)
    }

    fun provideGlideInstance(context: Context, requestOptions: RequestOptions)
            : RequestManager {
        return Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
    }

}
