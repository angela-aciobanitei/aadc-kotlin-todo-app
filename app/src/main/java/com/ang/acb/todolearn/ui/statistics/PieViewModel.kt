package com.ang.acb.todolearn.ui.statistics

import androidx.lifecycle.ViewModel

class PieViewModel : ViewModel() {

    val data = PieData()

    init {
        data.add("Sid", 18.0, "#4286f4")
        data.add("Nick", 4.0, "#44a837")
        data.add("Nick", 6.0, "#44a837")
        data.add("Dave", 10.0)
    }
}
