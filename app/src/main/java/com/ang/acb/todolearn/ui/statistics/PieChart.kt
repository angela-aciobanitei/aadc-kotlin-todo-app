package com.ang.acb.todolearn.ui.statistics

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * See: https://qa.blog.grio.com/2019/04/30/android-custom-views-creating-an-animated-pie-chart-from-scratch-part-1-of-4
 * See: https://qa.blog.grio.com/2019/07/android-custom-views-creating-an-animated-pie-chart-from-scratch-part-2-of-4
 * See: https://qa.blog.grio.com/2019/08/05/android-custom-views-creating-an-animated-pie-chart-from-scratch-part-3-of-4
 * See: https://qa.blog.grio.com/2019/08/12/android-custom-views-creating-an-animated-pie-chart-from-scratch-part-4-of-4
 */
class PieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Data
    private var data: PieData? = null

    // Used to paint the lines that divide the pie slices.
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = Color.WHITE
    }

    // Used to paint the indicator circle on each pie slice, which will be
    // connected to the indicator text with a straight line.
    private val indicatorCirclePaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.BLACK
    }

    // Used to paint the straight line connecting the indicator circle
    // on each pie slice to their associated text.
    private val indicatorLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = Color.BLACK
    }

    // Used to paint the indicator text for each pie slice,
    private val indicatorTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    // Used to set the radius of the indicator circle on each pie slice
    private var indicatorCircleRadius = 0f

    // Used to define the bounds of the complete pie chart circle.
    private val pieBounds = RectF()

    /**
     * Populates the data object and sets up the view based off the new data.
     * @param data the new set of data to be represented by the pie chart.
     */
    fun setData(data: PieData) {
        this.data = data
        setPieSliceDimensions()
        invalidate()
    }

    /**
     * Calculates and sets the dimensions of the pie slices in the pie chart.
     */
    private fun setPieSliceDimensions() {
        // We can think of the pie chart as a unit circle that goes from 0˚ to 360˚,
        // and the pie slices as arcs on that circle. First, we need to keep track
        // where the last slice ended and the next slice begins. This is what last
        // angle does.
        var lastAngle = 0f
        data?.pieSlices?.forEach {
            // Set the start angle of the current slice to the end of the last one.
            it.value.startAngle = lastAngle

            // Calculate the sweep angle: (currentValue / totalValueOfPieChart) * 360f
            it.value.sweepAngle = ((it.value.value / data?.totalValue!!) * 360f).toFloat()

            // Update last angle, so we know where to start the next slice.
            lastAngle += it.value.sweepAngle

            // Determine the coordinates for the indicator circle.
            setIndicatorCircleCoordinates(it.key)
        }
    }


    private fun setIndicatorCircleCoordinates(key: String) {
        data?.pieSlices?.get(key)?.let {
            // Find the middle angle of the current pie slice to calculate where
            // the indicator circle will be placed: (sweepAngle / 2) + startAngle
            val middleAngle = Math.toRadians((it.sweepAngle / 2 + it.startAngle).toDouble())

            // Calculate the distance the marker will be placed from the center
            // of the pie chart (here, 3/8ths of the pie chart distance from the edge)
            val fromPieCentre = 3 * layoutParams.height / 8

            // Calculate the x and y coordinates of the indicator circle.
            // Note: a point at angle theta on the circle whose centre is (x0,y0) and whose
            // radius is r has the following coordinates: (x0 + r cos theta, y0 + r sin theta)
            it.indicatorCirclePosition.x = fromPieCentre * cos(middleAngle).toFloat() + width / 2
            it.indicatorCirclePosition.y = fromPieCentre * sin(middleAngle).toFloat() + height / 2
        }
    }

    /**
     * Sets the bounds of the pie chart.
     *
     * @param top the top bound of the circle (the top of the view by default)
     * @param bottom the bottom bound of the circle (the bottom of view by default)
     * @param left the left bound of the circle (half of height by default)
     * @param right the right bound of the circle (half of height by default)
     */
    private fun setPieCircleBounds(
        top: Float = 0f,
        bottom: Float = layoutParams.height.toFloat(),
        left: Float = (width / 2) - (layoutParams.height / 2).toFloat(),
        right: Float = (width / 2) + (layoutParams.height / 2).toFloat()
    ) {
        pieBounds.top = top
        pieBounds.bottom = bottom
        pieBounds.left = left
        pieBounds.right = right
    }

    /**
     * Sets the text sizes and thickness of graphics used in the view.
     */
    private fun setGraphicSizes() {
        borderPaint.strokeWidth = height / 80f
        indicatorTextPaint.textSize = height / 15f
        indicatorLinePaint.strokeWidth = height / 120f
        indicatorCircleRadius = height / 70f
    }

    /**
     * Re-calculates graphic sizes if size of view is changed.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setPieCircleBounds()
        setGraphicSizes()
        data?.pieSlices?.forEach {
            setIndicatorCircleCoordinates(it.key)
        }
    }

    /**
     * Draws the view onto the screen.
     *
     * @param canvas canvas object to be used to draw
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        data?.pieSlices?.let { slices ->
            slices.forEach {
                // Draw the actual pie slice
                canvas?.drawArc(
                    pieBounds,
                    it.value.startAngle,
                    it.value.sweepAngle,
                    true,
                    it.value.paint
                )

                // Draw the pie slice border
                canvas?.drawArc(
                    pieBounds,
                    it.value.startAngle,
                    it.value.sweepAngle,
                    true,
                    borderPaint
                )

                // Draw the text, line and circle indicators.
                drawIndicators(canvas, it.value)
            }
        }
    }


    private fun drawIndicators(canvas: Canvas?, pieSlice: PieSlice) {
        // Draw the line and text indicators depending on which side of the
        // pie chart the circle indicator is situated on (left or right).
        if (pieSlice.indicatorCirclePosition.x < width / 2) {
            drawIndicatorLine(canvas, pieSlice, IndicatorAlignment.LEFT)
            drawIndicatorText(canvas, pieSlice, IndicatorAlignment.LEFT)
        }
        else {
            drawIndicatorLine(canvas, pieSlice, IndicatorAlignment.RIGHT)
            drawIndicatorText(canvas, pieSlice, IndicatorAlignment.RIGHT)
        }

        drawIndicatorCircle(canvas, pieSlice)
    }

    /**
     * Draws indicator lines onto the canvas depending on which side the of the pie the slice is on.
     */
    private fun drawIndicatorLine(canvas: Canvas?, pieSlice: PieSlice, alignment: IndicatorAlignment) {
        val xOffset = when (alignment) {
            IndicatorAlignment.LEFT -> width / 4 * - 1
            else -> width / 4
        }

        canvas?.drawLine(
            pieSlice.indicatorCirclePosition.x,
            pieSlice.indicatorCirclePosition.y,
            pieSlice.indicatorCirclePosition.x + xOffset,
            pieSlice.indicatorCirclePosition.y,
            indicatorLinePaint
        )
    }

    /**
     * Draws indicator text onto the canvas dependent on which side the of the pie the slice is on.
     */
    private fun drawIndicatorText(canvas: Canvas?, pieSlice: PieSlice, alignment: IndicatorAlignment) {
        val xOffset: Int
        val yOffset: Int = -25 // distance between text an line
        when (alignment) {
            IndicatorAlignment.LEFT -> {
                indicatorTextPaint.textAlign = Paint.Align.LEFT
                xOffset = width / 4 * - 1
            }
            else -> {
                indicatorTextPaint.textAlign = Paint.Align.RIGHT
                xOffset = width / 4
            }
        }

        canvas?.drawText(
            pieSlice.indicatorText,
            pieSlice.indicatorCirclePosition.x + xOffset,
            pieSlice.indicatorCirclePosition.y  + yOffset,
            indicatorTextPaint
        )
    }


    private fun drawIndicatorCircle(canvas: Canvas?, pieSlice: PieSlice) {
        canvas?.drawCircle(
            pieSlice.indicatorCirclePosition.x,
            pieSlice.indicatorCirclePosition.y,
            indicatorCircleRadius,
            indicatorCirclePaint
        )
    }
}