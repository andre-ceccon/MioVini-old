package br.com.miovini.utils.swipe

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import br.com.miovini.R
import kotlin.math.abs

object SwipeBackgroundHelper {

    private const val OFFSET_PX = 20
    private const val THRESHOLD = 2.5

    @JvmStatic
    fun paintDrawCommandToStart(
        canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float
    ) {
        val drawCommand = createDrawCommand(viewItem, dX, iconResId, viewItem.context)
        paintDrawCommand(drawCommand, canvas, dX, viewItem)
    }

    private fun createDrawCommand(
        viewItem: View, dX: Float, iconResId: Int, context: Context
    ): DrawCommand {
        var icon = ContextCompat.getDrawable(context, iconResId)

        icon = DrawableCompat.wrap(icon!!).mutate()
        icon.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(context, R.color.design_default_color_on_secondary), PorterDuff.Mode.SRC_IN
        )

        val backgroundColor = getBackgroundColor(
            R.color.design_default_color_primary,
            R.color.design_default_color_error,
            dX, viewItem
        )
        return DrawCommand(icon, backgroundColor)
    }

    private fun getBackgroundColor(
        firstColor: Int,
        secondColor: Int,
        dX: Float,
        viewItem: View
    ): Int {
        return when (willActionBeTriggered(dX, viewItem.width)) {
            true -> ContextCompat.getColor(viewItem.context, firstColor)
            false -> ContextCompat.getColor(viewItem.context, secondColor)
        }
    }

    private fun paintDrawCommand(
        drawCommand: DrawCommand,
        canvas: Canvas,
        dX: Float,
        viewItem: View
    ) {
        drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
        drawIcon(canvas, viewItem, dX, drawCommand.icon)
    }

    private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable) {
        val topMargin = calculateTopMargin(icon, viewItem)
        icon.bounds = getStartContainerRectangle(viewItem, icon.intrinsicWidth, topMargin, dX)
        icon.draw(canvas)
    }

    private fun getStartContainerRectangle(
        viewItem: View, iconWidth: Int, topMargin: Int, dx: Float
    ): Rect {
        val leftBound = viewItem.right + dx.toInt() + OFFSET_PX
        val rightBound = viewItem.right + dx.toInt() + iconWidth + OFFSET_PX
        val topBound = viewItem.top + topMargin
        val bottomBound = viewItem.bottom - topMargin

        return Rect(leftBound, topBound, rightBound, bottomBound)
    }

    private fun calculateTopMargin(icon: Drawable, viewItem: View): Int {
        return (viewItem.height - icon.intrinsicHeight) / 2
    }

    private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = color
        val backgroundRectangle = getBackGroundRectangle(viewItem, dX)
        canvas.drawRect(backgroundRectangle, backgroundPaint)
    }

    private fun getBackGroundRectangle(viewItem: View, dX: Float): RectF {
        return RectF(
            viewItem.right.toFloat() + dX,
            viewItem.top.toFloat(), viewItem.right.toFloat(),
            viewItem.bottom.toFloat()
        )
    }

    private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
        return abs(dX) >= viewWidth / THRESHOLD
    }

    private class DrawCommand(val icon: Drawable, val backgroundColor: Int)
}