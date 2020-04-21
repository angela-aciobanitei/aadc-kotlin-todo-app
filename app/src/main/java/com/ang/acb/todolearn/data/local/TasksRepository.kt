package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class TasksRepository(
    private val tasksLocalDataSource: TasksLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // Note: The `coroutineScope` builder creates a coroutine scope and
    // does not complete until all launched children complete.
    // https://kotlinlang.org/docs/reference/coroutines/basics.html#scope-builder
    suspend fun saveTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.saveTask(task) }
    }

    suspend fun updateTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.updateTask(task) }
    }

    suspend fun activateTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.activateTask(task) }
    }

    suspend fun completeTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.completeTask(task) }
    }

    suspend fun deleteTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.deleteTask(task) }
    }

    suspend fun deleteTaskById(taskId: Int) = coroutineScope {
        launch { tasksLocalDataSource.deleteTaskById(taskId) }
    }

    suspend fun deleteAllTasks() = coroutineScope {
        launch { tasksLocalDataSource.deleteAllTasks() }
    }

    suspend fun deleteCompletedTasks() = coroutineScope {
        launch { tasksLocalDataSource.deleteCompletedTasks() }
    }

    suspend fun getTask(taskId: Int): Result<Task> = tasksLocalDataSource.getTask(taskId)

    suspend fun getTasks(): Result<List<Task>> = tasksLocalDataSource.getTasks()

    fun getLiveTask(taskId: Int): LiveData<Result<Task>> = tasksLocalDataSource.getLiveTask(taskId)

    fun getLiveTasks(): LiveData<Result<List<Task>>> = tasksLocalDataSource.getLiveTasks()
}