package com.example.books.api

import android.content.Context
import android.util.Log
import com.example.books.model.Book
import com.example.books.util.isOnline
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

private const val TITLE = "intitle:"
private const val AUTHOR = "inauthor:"
private const val PUBLISHER = "inpublisher:"
private const val ISBN = "isbn:"

suspend fun searchBooks(
        context: Context,
        service: BookService,
        title: String?,
        author: String?,
        publisher: String?,
        isbn: String?,
        max: Int,
        key: String
): List<Book>{
    Timber.d("title: $title, max: $max")
    val sb = StringBuilder()
    if (!title.isNullOrBlank()) sb.append("$TITLE$title+")
    if (!author.isNullOrBlank()) sb.append("$AUTHOR$author+")
    if (!publisher.isNullOrBlank()) sb.append("$PUBLISHER$publisher+")
    if (!isbn.isNullOrBlank()) sb.append("$ISBN$isbn+")
    sb.setLength(sb.length - 1)
    val apiQuery = sb.toString()

    return if (context.isOnline()) {
        val request = service.searchBooks(apiQuery, key, max)
        Timber.d("Fetching from remote...")
        val response = request.await()
        response.items
    } else {
        emptyList()
    }


}

interface BookService {
    @GET("volumes")
    fun searchBooks(
            @Query("q") query: String,
            @Query("key") apiKey: String,
            @Query("maxResults") max: Int
    ): Deferred<BookSearchResponse>

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"

        fun create(): BookService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
                    .create(BookService::class.java)
        }
    }
}