package com.example.books.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.books.data.model.Book
import com.example.books.data.model.BookSearchResult
import com.example.books.data.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

class BookListViewModel(private val repository: BooksRepository) : ViewModel() {

//    private var title = ""
//    private var author = ""
//    private var publisher = ""
//    private var isbn = ""
//    private val queryLiveData = MutableLiveData<List<String?>>()
//    private val bookResult: LiveData<BookSearchResult> = Transformations.map(queryLiveData) { query ->
//        title = query[0] ?: ""
//        if (query.size > 1) {
//            author = query[1] ?: ""
//            publisher = query[2] ?: ""
//            isbn = query[3] ?: ""
//        } else {
//            author = ""
//            publisher = ""
//            isbn = ""
//        }
//        repository.search(title, author, publisher, isbn)
//    }

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Book>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<Book>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Book>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}