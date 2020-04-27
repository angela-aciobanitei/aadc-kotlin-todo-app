package com.ang.acb.todolearn

import android.app.Application
import com.ang.acb.todolearn.data.repo.TasksRepository
import timber.log.Timber

class TasksApplication : Application() {

    val taskRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}