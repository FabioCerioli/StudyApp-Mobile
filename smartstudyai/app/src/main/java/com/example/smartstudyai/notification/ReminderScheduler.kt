package com.example.smartstudyai.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        title: String,
        dayOfWeek: Int,
        hour: Int,
        minute: Int
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(
                context,
                StudyReminderReceiver::class.java
            ).apply {
                putExtra("title", title)
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                title.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        or PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        if (calendar.timeInMillis < System.currentTimeMillis())
        { calendar.add(Calendar.WEEK_OF_YEAR, 1) }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }
}