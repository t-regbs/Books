package com.example.books.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.books.databinding.FragmentBookDetailBinding

class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentBookDetailBinding =
                FragmentBookDetailBinding.inflate(inflater, container, false)

        val safeArgs: BookDetailFragmentArgs by navArgs()
        val book = safeArgs.book
        binding.book = book

        return binding.root
    }
}