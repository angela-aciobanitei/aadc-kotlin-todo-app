package com.ang.acb.todolearn.ui.statistics

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import java.util.*
import kotlin.collections.HashMap


enum class IndicatorAlignment {
    LEFT, RIGHT
}

/**
 * Model for a single pie slice
 *
 * @param label: the data label that will appear next to the pie slice (active/completed tasks)
 * @param value: the data value that the pie slice represents
 * @param startAngle: the angle that the arc of the pie slice will begin at
 * @param sweepAngle: the number of degrees the arc of the pie slice will travel
 * @param markerPosition: the position of the marker on each pie slice, which will be
 *                        connected to label and value with a straight line.
 * @param paint: the paint used to draw the pie slice.
 */
data class PieSlice(
    val label: String,
    var value: Double,
    var startAngle: Float,
    var sweepAngle: Float,
    var markerPosition: PointF,
    val paint: Paint
)


/**
 * This class will contain formatted data for the PieChart view. This will ensure that
 * PieChart always has a consistent type of data coming in, and will allow users to
 * easily assign, add, and delete data from the pie chart.
 */
class PieData {
    val pieSlices = HashMap<String, PieSlice>()
    // Keeps track of the total value to be associated with the PieChart.
    // This is important when calculating the dimensions of the pie slices.
    var totalValue = 0.0

    fun add(label: String, value: Double, color: String? = null) {
        if (pieSlices.containsKey(label)) {
            pieSlices[label]?.let { it.value += value }
        } else {
            color?.let {
                pieSlices[label] = PieSlice(label, value, 0f, 0f, PointF(), createPaint(it))
            } ?: run {
                pieSlices[label] = PieSlice(label, value, 0f, 0f, PointF(), createPaint(null))
            }
        }
        totalValue += value
    }

    /**
     * Dynamically create paints for a given pie slice.
     * If no color is passed, assign a random color.
     */
    private fun createPaint(color: String?): Paint {
        val newPaint = Paint()
        color?.let {
            newPaint.color = Color.parseColor(color)
        } ?: run {
            val randomValue = Random()
            newPaint.color = Color.argb(
                255,                       // alpha
                randomValue.nextInt(255), // red
                randomValue.nextInt(255), // green
                randomValue.nextInt(255)  // blue
            )
        }
        newPaint.isAntiAlias = true
        return newPaint
    }
}