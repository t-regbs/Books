package com.example.books.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class BooksLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<BooksLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: BooksLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BooksLoadStateViewHolder {
        return BooksLoadStateViewHolder.create(parent, retry)
    }
}