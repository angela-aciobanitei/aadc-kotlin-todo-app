package com.ang.acb.todolearn.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.ui.details.AddEditTaskViewModel
import com.ang.acb.todolearn.ui.details.TaskDetailsViewModel
import com.ang.acb.todolearn.ui.statistics.PieViewModel
import com.ang.acb.todolearn.ui.statistics.StatisticsViewModel


/**
 * A [ViewModelProvider.Factory] for creating view models for this app.
 */
class ViewModelFactory(
    private val tasksRepository: ITasksRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditTaskViewModel::class.java) -> {
                AddEditTaskViewModel(tasksRepository) as T
            }
            modelClass.isAssignableFrom(TaskDetailsViewModel::class.java) -> {
                TaskDetailsViewModel(tasksRepository) as T
            }
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) -> {
                StatisticsViewModel(tasksRepository) as T
            }
            modelClass.isAssignableFrom(PieViewModel::class.java) -> {
                PieViewModel(tasksRepository) as T
            }
            else -> throw IllegalArgumentException("Unable to construct view model")
        }
    }
}