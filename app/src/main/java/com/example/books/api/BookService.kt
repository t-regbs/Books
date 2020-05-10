package com.example.books.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET
    fun searchBooks(@Query("q") title: String)
}