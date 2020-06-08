package com.example.books.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.books.R
import com.example.books.databinding.FragmentBookDetailBinding
import com.example.books.ui.view.CustomPainter
import com.example.books.ui.view.ProfileCardPainter
import com.example.books.util.resToPx
import com.example.books.util.toColorInt

class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentBookDetailBinding =
                FragmentBookDetailBinding.inflate(inflater, container, false)

        val safeArgs: BookDetailFragmentArgs by navArgs()
        val book = safeArgs.book
        binding.book = book

        val azureColor = R.color.shapeColor.toColorInt(requireContext())
        val avatarRadius = R.dimen.avatar_radius.resToPx(requireContext())
        val avatarMargin = R.dimen.avatar_margin.resToPx(requireContext())
        val cardWidth = ViewGroup.LayoutParams.MATCH_PARENT
        val cardHeight = R.dimen.profile_card_height.resToPx(requireContext()).toInt()

        val painter = ProfileCardPainter(
                color = azureColor,
                avatarRadius = avatarRadius,
                avatarMargin = avatarMargin
        )

        binding.profileCardContainer.addView(
                CustomPainter(
                        context = requireContext(),
                        width = cardWidth,
                        height = cardHeight,
                        painter = painter
                )
        )

        return binding.root
    }
}