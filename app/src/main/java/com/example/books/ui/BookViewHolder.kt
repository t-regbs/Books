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
import com.example.books.model.Book

class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    private val tvAuthors: TextView = view.findViewById(R.id.tvAuthors)
    private val tvDate: TextView = view.findViewById(R.id.tvPublishedDate)
    private val tvPublisher: TextView = view.findViewById(R.id.tvPublisher)
    private val imageList: ImageView = view.findViewById(R.id.imgList)

    private var book: Book? = null

    init {
        view.setOnClickListener {
            book?.let {
                view.findNavController().navigate(BookListFragmentDirections.actionBooklistDestToBookDetailDest(it))
            }
        }
    }

    fun bind(book: Book?) {
        if (book == null) {
            val resources = itemView.resources
            tvTitle.text = resources.getString(R.string.loading)
            tvAuthors.visibility = View.GONE
            tvDate.visibility = View.GONE
            tvPublisher.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(book)
        }
    }

    private fun showRepoData(book: Book) {
        this.book = book
        tvTitle.text = book.volumeInfo.title
        Injection.provideGlideInstance(itemView.context, Injection.provideRequestOptions())
                .load(book.volumeInfo.imageLinks.thumbnail)
                .into(imageList)
        tvAuthors.text = book.volumeInfo.authors?.get(0) ?: ""
        tvDate.text = book.volumeInfo.publishedDate
        tvPublisher.text = book.volumeInfo.publisher
    }

    companion object {
        fun create(parent: ViewGroup): BookViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.book_list_item, parent, false)
            return BookViewHolder(view)
        }
    }

}