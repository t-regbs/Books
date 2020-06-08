package com.example.books.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircularImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  private var viewWidth: Int = 0
  private var viewHeight: Int = 0
  private var paint = Paint()

  @SuppressLint("DrawAllocation")
  public override fun onDraw(canvas: Canvas) {

    val bitmapDrawable = drawable as? BitmapDrawable
    bitmapDrawable?.bitmap?.let {
      paint.shader = BitmapShader(
          Bitmap.createScaledBitmap(it, width, height, false),
          Shader.TileMode.CLAMP,
          Shader.TileMode.CLAMP
      )
      val circleCenter = viewWidth / 2
      canvas.drawCircle(
          circleCenter.toFloat(),
          circleCenter.toFloat(),
          circleCenter.toFloat(),
          paint
      )
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    viewWidth = measureWidth(widthMeasureSpec)
    viewHeight = measureHeight(heightMeasureSpec)
    setMeasuredDimension(viewWidth, viewHeight)
  }

  private fun measureWidth(measureSpec: Int): Int {
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    return if (specMode == MeasureSpec.EXACTLY) {
      specSize
    } else {
      viewWidth
    }
  }

  private fun measureHeight(measureSpecHeight: Int): Int {
    val specMode = MeasureSpec.getMode(measureSpecHeight)
    val specSize = MeasureSpec.getSize(measureSpecHeight)
    return if (specMode == MeasureSpec.EXACTLY) {
      specSize
    } else {
      viewHeight
    }
  }
}
