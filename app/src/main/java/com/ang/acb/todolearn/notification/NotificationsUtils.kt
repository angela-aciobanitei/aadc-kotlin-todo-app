package com.ang.acb.todolearn.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.ui.common.MainActivity
import com.ang.acb.todolearn.ui.details.TaskDetailsFragmentArgs

const val WORKER_DATA_TASK_ID = "WORKER_DATA_TASK_ID"
const val WORKER_DATA_TASK_TITLE = "WORKER_DATA_TASK_TITLE"
const val NOTIFICATION_ID = 32


/**
 * An extension function that creates the tasks notification channel.
 */
fun NotificationManager.createTasksChannel(applicationContext: Context) {
    // Caution: notification channels APIs are not available in the support library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            applicationContext.getString(R.string.task_notification_channel_id),
            applicationContext.getString(R.string.task_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // Customize channel's default notification behaviors.
            description = applicationContext.getString(R.string.task_notification_channel_description)
            setShowBadge(false)
            enableVibration(true)
            enableLights(true)
            lightColor = Color.RED
        }

        // Register the notification channel with the system
        createNotificationChannel(notificationChannel)
    }
}


/**
 * An extension function that builds and delivers the notification.
 */
fun NotificationManager.sendNotification(
    applicationContext: Context,
    taskId: String,
    taskTitle: String
) {
    // For setting notification's tap action.
    // https://developer.android.com/guide/navigation/navigation-deep-link#explicit
    val args = TaskDetailsFragmentArgs(taskId).toBundle()
    val pendingIntent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.taskDetailsFragment)
        .setArguments(args)
        .createPendingIntent()

    // Create the notification builder using NotificationCompat.Builder()
    // and passing in the application context and the unique channel ID (string).
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.task_notification_channel_id)
    )
        // The priority is required (for Android 7.1 and lower).
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        // The small icon is required.
        .setSmallIcon(R.drawable.ic_mood)
        // The title and the text message are optional.
        .setContentTitle(applicationContext.getString(R.string.task_notification_title))
        .setContentText(taskTitle)
        // Add tap action.
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Build the notification.
    val notification = builder.build()

    // Deliver the notification
    notify(NOTIFICATION_ID, notification)
}


/**
 * An extension function that cancels all the notifications.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}