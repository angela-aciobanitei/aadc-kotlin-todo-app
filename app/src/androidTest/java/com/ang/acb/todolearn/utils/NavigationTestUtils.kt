package com.ang.acb.todolearn.utils

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import com.ang.acb.todolearn.R

fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String {
    var description = ""
    onActivity {
        description = it.findViewById<Toolbar>(R.id.main_toolbar).navigationContentDescription as String
    }
    return description
}