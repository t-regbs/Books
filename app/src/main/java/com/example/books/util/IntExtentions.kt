package com.example.books.util

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

@ColorInt
fun @receiver:ColorRes Int.toColorInt(context: Context): Int = ContextCompat.getColor(context, this)

fun Int.resToPx(context: Context): Float = context.resources.getDimension(this)
