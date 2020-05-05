package com.ang.acb.todolearn

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.util.NightMode
import timber.log.Timber
import java.util.*

class TasksApplication : Application() {

    val taskRepository: ITasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setupPreferences()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupPreferences() {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)

        preferences.getString(
            getString(R.string.theme_pref_key),
            getString(R.string.theme_pref_entry_night_auto)
        )?.apply {
            val mode = NightMode.valueOf(this.toUpperCase(Locale.US))
            AppCompatDelegate.setDefaultNightMode(mode.value)
        }
    }
}