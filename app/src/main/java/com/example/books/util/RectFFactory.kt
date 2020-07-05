package com.example.books.util

import android.graphics.PointF
import android.graphics.RectF

object RectFFactory {
    fun fromLTRB(left: Float, top: Float, right: Float, bottom: Float) =
            RectF(left, top, right, bottom)

    fun fromLTWH(left: Float, top: Float, width: Float, height: Float) =
            fromLTRB(left, top, left + width, top + height)

    fun fromCircle(center: PointF, radius: Float) =
            fromCenter(
                    center = center,
                    width = radius * 2,
                    height = radius * 2
            )

    fun fromCenter(center: PointF, width: Float, height: Float) =
            fromLTRB(
                    center.x - width / 2,
                    center.y - height / 2,
                    center.x + width / 2,
                    center.y + height / 2
            )
}
/**
Returns a new rectangle with edges moved outwards by the given delta.
 */
fun RectF.inflate(delta: Float): RectF {
    return RectFFactory.fromLTRB(
            left - delta,
            top - delta,
            right + delta,
            bottom + delta
    )
}

data class Size(val width: Float, val height: Float)