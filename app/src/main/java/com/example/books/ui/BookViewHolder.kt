package com.example.books.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.books.util.Injection
//import com.example.books.BookDetail
import com.example.books.R
import com.example.books.databinding.BookListItemBinding
import com.example.books.model.Book

class BookViewHolder(private val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(listener: View.OnClickListener, item: Book) {
        binding.apply {
            clickListener = listener
            book = item
            executePendingBindings()
        }
        Injection.provideGlideInstance(itemView.context, Injection.provideRequestOptions())
                .load(binding.book?.volumeInfo?.imageLinks?.thumbnail)
                .into(binding.imgList)
    }

    companion object {
        fun create(parent: ViewGroup): BookViewHolder {
            val view = BookListItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
            return BookViewHolder(view)
        }
    }

}