package com.ang.acb.todolearn.ui.statistics

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.ang.acb.todolearn.R
import kotlin.math.min


class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View (context, attrs, defStyleAttr) {

    // Initialize the Paint object. This is a performance optimization,
    // since onDraw() is called for every screen refresh.
    private val piePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    private val shadowPaint = Paint(0).apply {
        color = 0x101010
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private var radius = 0.0f

    private var activeTasksColor = 0
    private var completedTasksColor = 0
    private var noTasksColor = 0

    init {
        applyStyledAttributes(context, attrs)
    }

    private fun applyStyledAttributes(context: Context, attrs: AttributeSet? ) {
        context.withStyledAttributes(attrs, R.styleable.PieChartView) {
            activeTasksColor = getColor(R.styleable.PieChartView_activeTasksColor, 0)
            completedTasksColor = getColor(R.styleable.PieChartView_completedTasksColor, 0)
            noTasksColor = getColor(R.styleable.PieChartView_noTasksColor, 0)
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        // Account for padding
        val ww = width.toFloat() - (paddingLeft + paddingRight).toFloat()
        val hh = height.toFloat() - (paddingTop + paddingBottom).toFloat()

        // Figure out how big we can make the pie.
        val diameter = min(ww, hh)
        radius = (diameter / 2.0).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // To draw the circle, call drawCircle() passing in:
        // the x-coordinate of the center of the circle to be drawn,
        // the y-coordinate of the center of the circle to be drawn,
        // the radius of the circle to be drawn,
        // the paint used to draw the circle.
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, piePaint)
    }
}