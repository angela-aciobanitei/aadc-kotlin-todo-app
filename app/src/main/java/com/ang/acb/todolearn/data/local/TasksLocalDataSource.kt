package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import androidx.room.RoomDatabase
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a [RoomDatabase].
 */
class TasksLocalDataSource(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insert(task)
    }

    suspend fun saveTasks(tasks: List<Task>) = withContext(ioDispatcher) {
        tasksDao.insertAll(tasks)
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

    suspend fun deleteTaskById(taskId: String) = withContext(ioDispatcher) {
        tasksDao.deleteTaskById(taskId)
    }

    suspend fun deleteAllTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteAllTasks()
    }

    suspend fun deleteCompletedTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
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

    fun getLiveTask(taskId: String): LiveData<Result<Task>> {
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

    fun getAllPagedTasks(): LiveData<PagedList<Task>> =
        tasksDao.getAllPagedTasks().toLiveData( Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_SIZE
        ))

    fun getCompletedPagedTasks(): LiveData<PagedList<Task>> =
        tasksDao.getCompletedPagedTasks().toLiveData( Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_SIZE
        ))

    fun getActivePagedTasks(): LiveData<PagedList<Task>> =
        tasksDao.getActivePagedTasks().toLiveData( Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_SIZE
        ))

    companion object {
        private const val PAGE_SIZE = 50
        private const val MAX_SIZE = 200
    }
}