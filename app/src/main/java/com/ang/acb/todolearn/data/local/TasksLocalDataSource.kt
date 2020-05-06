package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import androidx.room.RoomDatabase
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ang.acb.todolearn.notification.NotificationWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a [RoomDatabase].
 */
class TasksLocalDataSource(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksDataSource {

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insert(task)
        NotificationWorker.scheduleNotification(task)
    }

    override suspend fun saveTasks(tasks: List<Task>) = withContext(ioDispatcher) {
        tasksDao.insertAll(tasks)
    }

    override suspend fun updateTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.update(task)
        // No need to cancel previous work, @see ExistingWorkPolicy.KEEP
        NotificationWorker.scheduleNotification(task)
    }

    override suspend fun activateTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(taskId = task.id, isCompleted = false)
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        NotificationWorker.cancelWork(task)
        tasksDao.updateCompleted(taskId = task.id, isCompleted = true)
    }
    override suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        NotificationWorker.cancelWork(task)
        tasksDao.delete(task)
    }

    override suspend fun deleteTaskById(taskId: String) = withContext(ioDispatcher) {
        NotificationWorker.cancelWork(taskId)
        tasksDao.deleteTaskById(taskId)
    }

    override suspend fun deleteAllTasks() = withContext<Unit>(ioDispatcher) {
        NotificationWorker.cancelAllWork()
        tasksDao.deleteAllTasks()
    }

    override suspend fun deleteCompletedTasks() = withContext<Unit>(ioDispatcher) {
        // TODO Cancel notifications for these deleted tasks
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
        try {
            val task = tasksDao.getTaskById(taskId)
            if (task != null) return@withContext Result.Success(task)
            else return@withContext Result.Error(Exception("Task not found"))
        } catch (e:Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        try {
            val tasks = tasksDao.getTasks()
            if (tasks != null) return@withContext Result.Success(tasks)
            else return@withContext Result.Error(Exception("Tasks not found"))
        } catch (e:Exception) {
            return@withContext Result.Error(e)
        }
    }

    override fun getLiveTask(taskId: String): LiveData<Result<Task>> {
        // Note: instead of Transformations.map(), you can use the
        // map() function from the "androidx.lifecycle" library.
        val task = tasksDao.getLiveTaskById(taskId)
        return Transformations.map(task) {
            Result.Success(it)
        }
    }

    override fun getLiveTasks(): LiveData<Result<List<Task>>> =
        tasksDao.getLiveTasks().map {
            Result.Success(it)
        }

    override fun getAllPagedTasks(): LiveData<PagedList<Task>> =
        tasksDao.getAllPagedTasks().toLiveData( Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_SIZE
        ))

    override fun getCompletedPagedTasks(): LiveData<PagedList<Task>> =
        tasksDao.getCompletedPagedTasks().toLiveData( Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_SIZE
        ))

    override fun getActivePagedTasks(): LiveData<PagedList<Task>> =
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