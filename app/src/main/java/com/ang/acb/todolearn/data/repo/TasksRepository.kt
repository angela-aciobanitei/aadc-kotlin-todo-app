package com.ang.acb.todolearn.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksDatabase
import com.ang.acb.todolearn.data.local.TasksLocalDataSource
import kotlinx.coroutines.*

class TasksRepository private constructor(
    private val tasksLocalDataSource: TasksLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        @Volatile
        private var INSTANCE: TasksRepository? = null

        fun getInstance(context: Context): TasksRepository {
            synchronized(this) {
                var repository =
                    INSTANCE
                if (repository == null) {
                    repository = TasksRepository(
                        TasksLocalDataSource(
                            TasksDatabase.getInstance(
                                context
                            ).tasksDao, Dispatchers.IO
                        )
                    )

                    INSTANCE = repository
                }

                return repository
            }
        }
    }

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
}