package com.example.books.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class BookSearchResult(
    val data: LiveData<PagedList<Book>>,
    val networkErrors: LiveData<String>,
    val loadingData: LiveData<Boolean>
)