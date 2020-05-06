package com.ang.acb.todolearn.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.work.*
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.NO_DEADLINE
import com.ang.acb.todolearn.data.local.Task
import java.util.concurrent.TimeUnit


/**
 * Run a work to show a notification on a background thread by the {@link WorkManger}.
 */
class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    // Get the input data for this work
    private val taskId = inputData.getString(WORKER_DATA_TASK_ID)
    private val taskTitle = inputData.getString(WORKER_DATA_TASK_TITLE)

    override fun doWork(): Result {
        val prefManager = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(
            applicationContext.getString(R.string.notifications_pref_key),
            false
        )

        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java) as NotificationManager

        // TODO if(shouldNotify)...
        notificationManager.sendNotification(
            applicationContext,
            taskTitle ?: applicationContext.getString(R.string.task_notification_message)
        )

        return Result.success()
    }

    companion object {

        fun scheduleNotification(task:Task) {
            // Tasks with no deadline should not try to send a notification
            if (task.deadline == NO_DEADLINE) return

            // This data will be sent to the WorkManager
            val inputData = Data.Builder()
                .putString(WORKER_DATA_TASK_ID, task.id)
                .putString(WORKER_DATA_TASK_TITLE, task.title)
                .build()

            val delay = task.deadline - System.currentTimeMillis()

            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .build()

            WorkManager.getInstance().enqueueUniqueWork(
                // A unique work name
                task.id,
                // If there is existing pending (uncompleted) work with
                // the same unique name, cancel and delete it.  Then,
                // insert the newly-specified work.
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )
        }

        fun cancelWork(task: Task) {
            WorkManager.getInstance().cancelUniqueWork(task.id)
        }

        fun cancelWork(tasks: List<Task>) {
            for (task in tasks) {
                WorkManager.getInstance().cancelUniqueWork(task.id)
            }
        }

        fun cancelWork(taskId: String) {
            WorkManager.getInstance().cancelUniqueWork(taskId)
        }

        fun cancelAllWork() {
            WorkManager.getInstance().cancelAllWork()
        }
    }
}