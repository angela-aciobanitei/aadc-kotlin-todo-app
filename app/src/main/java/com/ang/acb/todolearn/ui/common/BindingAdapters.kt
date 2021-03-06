package com.ang.acb.todolearn.ui.common

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun View.showHide(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
