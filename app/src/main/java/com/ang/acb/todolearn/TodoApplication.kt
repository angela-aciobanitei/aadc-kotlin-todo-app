package com.ang.acb.todolearn

import android.app.Application
import timber.log.Timber

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Plant a tree
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}