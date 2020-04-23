package com.ang.acb.todolearn.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.util.sendNotification

class AlarmReceiver :BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Send notification
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.notification_message).toString(),
            context
        )
    }
}
