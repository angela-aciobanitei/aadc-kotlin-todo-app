package com.ang.acb.todolearn

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.ang.acb.todolearn.data.local.TasksDatabase
import com.ang.acb.todolearn.data.local.TasksLocalDataSource
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.data.repo.TasksRepository
import kotlinx.coroutines.runBlocking


/**
 * A Service Locator is an alternative to dependency injection. The service locator
 * design pattern also improves decoupling of classes from concrete dependencies.
 * You create a class known as the service locator that creates and stores dependencies
 * and then provides those dependencies on demand.
 *
 * See: https://developer.android.com/training/dependency-injection#di-alternatives
 */
object ServiceLocator {

    @Volatile
    var tasksRepository : ITasksRepository? = null
        // Setter is public only for testing
        @VisibleForTesting set

    private var database : TasksDatabase? = null
    private val lock = Any()

    fun provideTasksRepository(context: Context) : ITasksRepository {
        // Thread safety
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context) : ITasksRepository {
        val newRepository = TasksRepository(createTasksLocalDataSource(context))
        tasksRepository = newRepository
        return newRepository
    }

    private fun createTasksLocalDataSource(context: Context) : TasksLocalDataSource{
        val tasksDatabase = database ?: createDb(context)
        return TasksLocalDataSource(tasksDatabase.tasksDao())
    }

    private fun createDb(context: Context): TasksDatabase {
        val tasksDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    "tasks.db")
                    .fallbackToDestructiveMigration()
                    .build()
            database = tasksDatabase
            return tasksDatabase
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            runBlocking {
                database?.apply {
                    tasksDao().deleteAllTasks()
                    clearAllTables()
                    close()
                }
            }

            database = null
            tasksRepository = null
        }
    }
}