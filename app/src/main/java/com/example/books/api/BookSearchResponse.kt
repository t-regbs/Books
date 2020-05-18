package com.example.books.api

import com.example.books.model.Book
import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    @SerializedName("totalItems") val total: Int = 0,
    @SerializedName("items") val items: List<Book> = emptyList()
)