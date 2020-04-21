package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksViewModel : ViewModel() {

    private val _navigateToAddTask = MutableLiveData<Boolean>()
    val navigateToAddTask: LiveData<Boolean>
        get() = _navigateToAddTask

    fun navigateToAddTask() {
        _navigateToAddTask.value = true
    }

    fun navigateToAddTaskCompleted() {
        _navigateToAddTask.value = null
    }
}
