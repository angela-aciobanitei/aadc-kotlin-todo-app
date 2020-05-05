package com.ang.acb.todolearn.util

import androidx.appcompat.app.AppCompatDelegate


enum class NightMode(val value: Int) {

    // Night mode which uses a dark mode when the system's 'Battery Saver'
    // feature is enabled, otherwise it uses a 'light mode'. This mode can
    // help the device to decrease power usage.
    AUTO(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY),

    // Night mode which uses always uses a dark mode, enabling 'night'
    // qualified resources regardless of the time.
    ON(AppCompatDelegate.MODE_NIGHT_YES),

    // Night mode which uses always uses a light mode, enabling 'notnight'
    // qualified resources regardless of the time.
    OFF(AppCompatDelegate.MODE_NIGHT_NO)
}