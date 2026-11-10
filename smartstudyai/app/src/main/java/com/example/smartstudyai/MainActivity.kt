package com.example.smartstudyai


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.smartstudyai.data.database.AppDatabase
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.ScheduleRepository
import com.example.smartstudyai.data.repository.SensorRepository
import com.example.smartstudyai.data.repository.SessionRepository
import com.example.smartstudyai.navigation.AppNavigation
import com.example.smartstudyai.notification.NotificationHelper
import com.example.smartstudyai.sensors.SensorCollector
import com.example.smartstudyai.ui.theme.SmartstudyaiTheme
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?){
        NotificationHelper.createChannel(this)
        super.onCreate(savedInstanceState)
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {
            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
        val database =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "smartstudy.db"
            )
                .build()
        val activityRepository = ActivityRepository(database.activityDao())
        val sensorCollector = SensorCollector(this)
        val sensorRepository = SensorRepository(sensorCollector)
        val sessionRepository = SessionRepository(database.sessionDao())
        val scheduleRepository = ScheduleRepository(database.scheduleDao())
        setContent {
            SmartstudyaiTheme {
                AppNavigation(
                    activityRepository = activityRepository,
                    sensorRepository = sensorRepository,
                    sessionRepository = sessionRepository,
                    scheduleRepository = scheduleRepository
                )
            }
        }
    }
}