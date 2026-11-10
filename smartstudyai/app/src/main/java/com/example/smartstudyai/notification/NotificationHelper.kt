package com.example.smartstudyai.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {

    const val CHANNEL_ID = "study_reminders"

    fun createChannel(context: Context) {

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "Promemoria Studio",
                NotificationManager.IMPORTANCE_HIGH
            )

        val manager = context.getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(channel)
    }
}