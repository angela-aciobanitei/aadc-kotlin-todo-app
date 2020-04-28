package com.ang.acb.todolearn.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.repo.ITasksRepository


/**
 * The [ViewModel] for [PieFragment]
 */
class PieViewModel(private val tasksRepository: ITasksRepository) : ViewModel() {

    private val tasks  = tasksRepository.getLiveTasks()

    val stats = tasks.map { resultTasks ->
        if (resultTasks is Result.Success) {
            getTasksStats(resultTasks.data)
        } else {
            null
        }
    }
}
