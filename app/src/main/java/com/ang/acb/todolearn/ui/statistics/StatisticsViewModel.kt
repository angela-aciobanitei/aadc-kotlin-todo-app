package com.ang.acb.todolearn.ui.statistics

import androidx.lifecycle.*
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val tasks  = tasksRepository.getLiveTasks()

    private val stats = tasks.map { resultTasks ->
        if (resultTasks is Result.Success) {
            getTasksStats(resultTasks.data)
        } else {
            null
        }
    }

    val activeTasksPercent : LiveData<Float> = stats.map {
        it?.activeTasksPercent ?: 0f
    }

    val completedTasksPercent: LiveData<Float> = stats.map {
        it?.completedTasksPercent ?: 0f
    }

    val error: LiveData<Boolean> = tasks.map {
        it is Result.Error
    }

    val empty: LiveData<Boolean> = tasks.map {
        (it as? Result.Success)?.data.isNullOrEmpty()
    }
}
