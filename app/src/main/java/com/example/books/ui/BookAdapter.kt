package com.example.books.ui

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.books.model.Book

class BookAdapter : PagedListAdapter<Book, RecyclerView.ViewHolder>(BOOK_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = getItem(position)
        if (book != null) {
            (holder as BookViewHolder).apply {
                bind(createOnClickListener(book), book)
            }
        }
    }

    private fun createOnClickListener(book: Book): View.OnClickListener {
        return View.OnClickListener {
            val direction = BookListFragmentDirections.actionBooklistDestToBookDetailDest(book)
            it.findNavController().navigate(direction)
        }
    }

    companion object {
        private val BOOK_COMPARATOR = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
                    oldItem == newItem
        }
    }
}