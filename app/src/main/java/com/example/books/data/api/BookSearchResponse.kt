package com.example.books.data.api

import com.example.books.data.model.Book
import com.squareup.moshi.Json

data class BookSearchResponse(
    @Json(name = "totalItems") val total: Int = 0,
    @Json(name = "items") val items: List<Book> = emptyList()
)