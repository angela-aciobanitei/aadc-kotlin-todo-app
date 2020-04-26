package com.ang.acb.todolearn.ui.statistics

import androidx.lifecycle.*
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import kotlinx.coroutines.launch

class PieViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val tasks  = tasksRepository.getLiveTasks()

    val stats = tasks.map { resultTasks ->
        if (resultTasks is Result.Success) {
            getTasksStats(resultTasks.data)
        } else {
            null
        }
    }
}
