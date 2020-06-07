package com.example.books.api

import com.example.books.model.Book
import com.squareup.moshi.Json

data class BookSearchResponse(
        @Json(name = "totalItems") val total: Int = 0,
        @Json(name = "items") val items: List<Book> = emptyList()
)