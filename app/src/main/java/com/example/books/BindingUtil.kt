package com.example.books

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingUtil {
    @BindingAdapter("android:imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                    .load(imageUrl)
                    .into(view)
        } else {
            view.setBackgroundResource(R.drawable.book_open)
        }
    }
}