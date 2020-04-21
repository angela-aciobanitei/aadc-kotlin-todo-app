package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a database.
 */
class TasksLocalDataSource(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insert(task)
    }

    suspend fun updateTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.update(task)
    }

    suspend fun activateTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(taskId = task.id, isCompleted = false)
    }

    suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(taskId = task.id, isCompleted = true)
    }
    suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.delete(task)
    }

    suspend fun deleteTaskById(taskId: Int) = withContext(ioDispatcher) {
        tasksDao.deleteTaskById(taskId)
    }

    suspend fun deleteAllTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteAllTasks()
    }

    suspend fun deleteCompletedTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    suspend fun getTask(taskId: Int): Result<Task> = withContext(ioDispatcher) {
        try {
            val task = tasksDao.getTaskById(taskId)
            if (task != null) return@withContext Result.Success(task)
            else return@withContext Result.Error(Exception("Task not found"))
        } catch (e:Exception) {
            return@withContext Result.Error(e)
        }
    }

    suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        try {
            val tasks = tasksDao.getTasks()
            if (tasks != null) return@withContext Result.Success(tasks)
            else return@withContext Result.Error(Exception("Tasks not found"))
        } catch (e:Exception) {
            return@withContext Result.Error(e)
        }
    }

    fun getLiveTask(taskId: Int): LiveData<Result<Task>> {
        // Note: instead of Transformations.map(), you can use the
        // map() function from the "androidx.lifecycle" library.
        val task = tasksDao.getLiveTaskById(taskId)
        return Transformations.map(task) {
            Result.Success(it)
        }
    }

    fun getLiveTasks(): LiveData<Result<List<Task>>> = tasksDao.getLiveTasks().map {
        Result.Success(it)
    }
}