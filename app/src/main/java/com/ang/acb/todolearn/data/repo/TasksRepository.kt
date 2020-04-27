package com.ang.acb.todolearn.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksDataSource
import com.ang.acb.todolearn.data.local.TasksLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/**
 * Repository module for handling data operations.
 */
class TasksRepository (
    private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // Note: The `coroutineScope` builder creates a coroutine scope and
    // does not complete until all launched children complete.
    // https://kotlinlang.org/docs/reference/coroutines/basics.html#scope-builder
    suspend fun saveTask(task: Task) = coroutineScope {
        launch { tasksLocalDataSource.saveTask(task) }
    }

    suspend fun saveTasks(tasks: List<Task>) = coroutineScope {
        launch { tasksLocalDataSource.saveTasks(tasks) }
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

    suspend fun deleteTaskById(taskId: String) = coroutineScope {
        launch { tasksLocalDataSource.deleteTaskById(taskId) }
    }

    suspend fun deleteAllTasks() = coroutineScope {
        launch { tasksLocalDataSource.deleteAllTasks() }
    }

    suspend fun deleteCompletedTasks() = coroutineScope {
        launch { tasksLocalDataSource.deleteCompletedTasks() }
    }

    suspend fun getTask(taskId: String): Result<Task> = tasksLocalDataSource.getTask(taskId)

    suspend fun getTasks(): Result<List<Task>> = tasksLocalDataSource.getTasks()

    fun getLiveTask(taskId: String): LiveData<Result<Task>> = tasksLocalDataSource.getLiveTask(taskId)

    fun getLiveTasks(): LiveData<Result<List<Task>>> = tasksLocalDataSource.getLiveTasks()

    fun getAllPagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getAllPagedTasks()

    fun getCompletedPagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getCompletedPagedTasks()

    fun getActivePagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getActivePagedTasks()
}