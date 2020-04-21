package com.ang.acb.todolearn.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddEditTaskViewModel : ViewModel() {

    private val _taskUpdated = MutableLiveData<Boolean>()
    val taskUpdated: LiveData<Boolean>
        get() = _taskUpdated

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun start(taskId: Int, title: String) {

    }
}
