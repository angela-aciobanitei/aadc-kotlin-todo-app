package com.ang.acb.todolearn.ui.common

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun View.showHide(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}