package com.ang.acb.todolearn.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.ui.common.MainActivity

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
    notificationMessage: String
) {
    // For setting notification's tap action.
    val mainIntent = Intent(applicationContext, MainActivity::class.java)
    val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        mainIntent,
        // This intent can be reused; keep the existing
        // intent, and just update its extra data.
        PendingIntent.FLAG_UPDATE_CURRENT
    )

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
        .setContentText(notificationMessage)
        // Add tap action.
        .setContentIntent(mainPendingIntent)
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