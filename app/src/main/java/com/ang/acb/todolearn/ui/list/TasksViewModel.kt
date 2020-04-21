package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ang.acb.todolearn.data.local.TasksRepository

class TasksViewModel(val tasksRepository: TasksRepository) : ViewModel() {

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
