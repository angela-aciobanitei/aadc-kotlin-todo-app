package com.ang.acb.todolearn.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.ui.details.AddEditTaskViewModel
import com.ang.acb.todolearn.ui.list.TasksViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val tasksRepository: TasksRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java)) {
            return AddEditTaskViewModel(tasksRepository) as T
        } else if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(tasksRepository) as T
        }
        throw IllegalArgumentException("Unable to construct view model")
    }
}