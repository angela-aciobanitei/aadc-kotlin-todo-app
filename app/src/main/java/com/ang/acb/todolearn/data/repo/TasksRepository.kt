package com.ang.acb.todolearn.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksDataSource
import com.ang.acb.todolearn.data.local.TasksLocalDataSource
import kotlinx.coroutines.*


/**
 * Repository module for handling data operations.
 */
class TasksRepository (
    private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ITasksRepository {

    // Note: The `coroutineScope` builder creates a coroutine scope and
    // does not complete until all launched children complete.
    // https://kotlinlang.org/docs/reference/coroutines/basics.html#scope-builder
    override suspend fun saveTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        coroutineScope {
            launch { tasksLocalDataSource.saveTasks(tasks) }
        }
    }

    override suspend fun updateTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.updateTask(task) }
        }
    }

    override suspend fun activateTask(task: Task){
        coroutineScope {
            launch { tasksLocalDataSource.activateTask(task) }
        }
    }

    override suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    override suspend fun deleteTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.deleteTask(task) }
        }
    }

    override suspend fun deleteTaskById(taskId: String) {
        coroutineScope {
            launch { tasksLocalDataSource.deleteTaskById(taskId) }
        }
    }

    override suspend fun deleteAllTasks() {
        coroutineScope {
            launch { tasksLocalDataSource.deleteAllTasks() }
        }
    }

    override suspend fun deleteCompletedTasks() {
        coroutineScope {
            launch { tasksLocalDataSource.deleteCompletedTasks() }
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> = tasksLocalDataSource.getTask(taskId)

    override suspend fun getTasks(): Result<List<Task>> = tasksLocalDataSource.getTasks()

    override fun getLiveTask(taskId: String): LiveData<Result<Task>> = tasksLocalDataSource.getLiveTask(taskId)

    override fun getLiveTasks(): LiveData<Result<List<Task>>> = tasksLocalDataSource.getLiveTasks()

    override fun getAllPagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getAllPagedTasks()

    override fun getCompletedPagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getCompletedPagedTasks()

    override fun getActivePagedTasks(): LiveData<PagedList<Task>> = tasksLocalDataSource.getActivePagedTasks()
}