package com.example.books.ui.view

import android.graphics.*
import androidx.annotation.ColorInt
import com.example.books.util.*

class ProfileCardPainter(
    @ColorInt private val color: Int,
    private val avatarRadius: Float,
    private val avatarMargin: Float
): Painter {
    override fun paint(canvas: Canvas) {
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val shapeBounds = RectFFactory.fromLTWH(0f, 0f, width, height - avatarRadius)

        val centerAvatar = PointF(shapeBounds.centerX(), shapeBounds.bottom)
        val avatarBounds = RectFFactory.fromCircle(center = centerAvatar, radius =
        avatarRadius).inflate(avatarMargin)
        drawBackground(canvas, shapeBounds, avatarBounds)

        val curvedShapeBounds = RectFFactory.fromLTRB(
            shapeBounds.left,
            shapeBounds.top + shapeBounds.height() * 0.35f,
            shapeBounds.right,
            shapeBounds.bottom
        )
        drawCurvedShape(canvas, curvedShapeBounds, avatarBounds)

    }

    private fun drawBackground(canvas: Canvas, bounds: RectF, avatarBounds: RectF) {
        val paint = Paint()
        paint.color = color

        val backgroundPath = Path().apply {
            moveTo(bounds.left, bounds.top)
            lineTo(bounds.bottomLeft.x, bounds.bottomLeft.y)
            arcTo(avatarBounds, -180f, 180f, false)
            lineTo(bounds.bottomRight.x, bounds.bottomRight.y)
            lineTo(bounds.topRight.x, bounds.topRight.y)
            close()
        }
        canvas.drawPath(backgroundPath, paint);
    }

    private fun drawCurvedShape(canvas: Canvas, bounds: RectF, avatarBounds: RectF) {
        val paint = Paint()
        paint.shader = createGradient(bounds)

        val handlePoint = PointF(bounds.left + (bounds.width() * 0.25f), bounds.top)

        val curvePath = Path().apply {
            moveTo(bounds.bottomLeft.x, bounds.bottomLeft.y)
            lineTo(avatarBounds.centerLeft.x, avatarBounds.centerLeft.y)
            arcTo(avatarBounds, -180f, 180f, false)
            lineTo(bounds.bottomRight.x, bounds.bottomRight.y)
            lineTo(bounds.topRight.x, bounds.topRight.y)
            quadTo(handlePoint.x, handlePoint.y, bounds.bottomLeft.x, bounds.bottomLeft.y)
            close()
        }
        canvas.drawPath(curvePath, paint)
    }

    private fun createGradient(bounds: RectF): LinearGradient {
        val colors = intArrayOf(color.darkerShade(), color, color.darkerShade())
        val stops = floatArrayOf(0.0f, 0.3f, 1.0f)
        return LinearGradient(
            bounds.centerLeft.x, bounds.centerLeft.y,
            bounds.centerRight.x, bounds.centerRight.y,
            colors,
            stops,
            Shader.TileMode.REPEAT
        )
    }

}