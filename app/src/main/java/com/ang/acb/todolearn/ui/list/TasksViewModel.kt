package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksRepository
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for the [TasksFragment]
 */
class TasksViewModel(val tasksRepository: TasksRepository) : ViewModel() {

    private val _navigateToAddTask = MutableLiveData<Event<Unit>>()
    val navigateToAddTask: LiveData<Event<Unit>>
        get() = _navigateToAddTask

    fun navigateToAddTask() {
        _navigateToAddTask.value = Event(Unit)
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if(completed) tasksRepository.completeTask(task)
        else tasksRepository.activateTask(task)
    }
}
