package com.example.books.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import com.example.books.R
import com.example.books.ui.customview.CircularImageView
import org.koin.core.KoinComponent
import org.koin.core.inject

object GlideInstance : KoinComponent {
    val glide: RequestManager by inject()
}

object BindingUtil {
    @BindingAdapter("android:imageUrl")
    @JvmStatic
    fun loadImage(view: CircularImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            GlideInstance.glide
                    .load(imageUrl)
                    .into(view)
        } else {
            view.setBackgroundResource(R.drawable.book_open)
        }
    }

    @BindingAdapter("android:imageUrl")
    @JvmStatic
    fun loadNormalImage(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            GlideInstance.glide
                    .load(imageUrl)
                    .into(view)
        } else {
            view.setBackgroundResource(R.drawable.book_open)
        }
    }
}