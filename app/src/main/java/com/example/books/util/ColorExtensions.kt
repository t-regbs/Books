package com.example.books.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.math.MathUtils.clamp

@ColorInt
fun Int.darkerShade(): Int {
  val darkness = 10
  return rgb(
      clamp((Color.red(this) - darkness), 0, 255),
      clamp((Color.green(this) - darkness), 0, 255),
      clamp((Color.blue(this) - darkness), 0, 255)
  )
}

@ColorInt
private fun rgb(red: Int, green: Int, blue: Int): Int {
  return 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
}
