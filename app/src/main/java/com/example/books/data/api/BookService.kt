package com.example.books.data.api

import com.example.books.data.model.Book
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

const val TITLE = "intitle:"
const val AUTHOR = "inauthor:"
const val PUBLISHER = "inpublisher:"
const val ISBN = "isbn:"

suspend fun searchBooks(
    service: BookService,
    title: String?,
    author: String?,
    publisher: String?,
    isbn: String?,
    max: Int,
    key: String,
    page: Int = 0,
    onSuccess: suspend (books: List<Book>) -> Unit,
    onError: (error: String) -> Unit
) {
    Timber.d("title: $title, max: $max, page: $page")
    val sb = StringBuilder()
    if (!title.isNullOrBlank()) sb.append("$TITLE$title+")
    if (!author.isNullOrBlank()) sb.append("$AUTHOR$author+")
    if (!publisher.isNullOrBlank()) sb.append("$PUBLISHER$publisher+")
    if (!isbn.isNullOrBlank()) sb.append("$ISBN$isbn+")
    sb.setLength(sb.length - 1)
    val apiQuery = sb.toString()

    try {
        val apiResponse = service.searchBooks(apiQuery, key, max, page)
//        if (response.isSuccessful) {
//            Timber.d("got a response: $response")
//            response.body()?.let {
//                onSuccess(it.items)
//            }
//        } else {
//            Timber.d("failed to get a response: $response")
//            onError(response.errorBody().toString())
//        }
    } catch (err: Throwable) {
        Timber.d("failed to get a response with error $err")
        onError(err.message ?: "unknown error")
    }
}

interface BookService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("maxResults") max: Int,
        @Query("startIndex") page: Int
    ): BookSearchResponse
}