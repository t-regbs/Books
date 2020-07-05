package com.example.books.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.annotation.Dimension.PX

@SuppressLint("ViewConstructor")
class CustomPainter(
    context: Context,
    @Dimension(unit = PX) width: Int,
    @Dimension(unit = PX) height: Int,
    private val painter: Painter
) : View(context) {

    init {
        layoutParams = ViewGroup.LayoutParams(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let(painter::paint)
    }
}