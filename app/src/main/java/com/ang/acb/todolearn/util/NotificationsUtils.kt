package com.ang.acb.todolearn.util

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

private const val NOTIFICATION_ID = 0

/**
 * An extension function that creates a notification channel.
 *
 * @param channelId The id of the channel. Must be unique per package.
 * @param channelName The user visible name of the channel.
 * @param channelDescription The user visible description of the channel.
 */
fun NotificationManager.createChannel(
    channelId: String,
    channelName: String,
    channelDescription: String
) {
    // Caution: notification channels APIs are not available in the support library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // TODO: Create a NotificationChannel object with an unique
        //  channel ID, a user-visible name, and an importance level.
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // Customize channel's default notification behaviors.
            description = channelDescription
            setShowBadge(false)
            enableVibration(true)
            enableLights(true)
            lightColor = Color.RED
        }

        // TODO: Register the notification channel with the system by calling
        //  createNotificationChannel() and passing in the newly created channel.
        createNotificationChannel(notificationChannel)
    }
}


/**
 * An extension function that builds and delivers the notification.
 *
 * @param applicationContext, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
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

    // TODO: Create the notification builder using NotificationCompat.Builder()
    //  and passing in the application context and the unique channel ID (string).
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.task_notification_channel_id)
    )
        // The priority is required (to support Android 7.1 and lower).
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        // The small icon is required.
        .setSmallIcon(R.drawable.ic_mood)
        // The title and the text message are optional.
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        // Add tap action.
        .setContentIntent(mainPendingIntent)
        .setAutoCancel(true)


    // TODO: Build the notification.
    val notification = builder.build()

    // TODO: Deliver the notification by calling notify() and passing
    //  in an unique notification ID (int) and the notification object.
    notify(NOTIFICATION_ID, notification)
}


/**
 * An extension function that cancels all notifications.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}