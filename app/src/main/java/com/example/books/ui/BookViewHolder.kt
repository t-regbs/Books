package com.example.books.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.books.databinding.BookListItemBinding
import com.example.books.data.model.Book

class BookViewHolder(private val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: View.OnClickListener, item: Book) {
        binding.apply {
            clickListener = listener
            book = item
            executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup): BookViewHolder {
            val view = BookListItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
            return BookViewHolder(view)
        }
    }
}