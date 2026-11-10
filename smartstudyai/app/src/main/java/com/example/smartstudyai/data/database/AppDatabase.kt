package com.example.smartstudyai.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartstudyai.data.dao.ActivityDao
import com.example.smartstudyai.data.dao.ScheduleDao
import com.example.smartstudyai.data.dao.SessionDao
import com.example.smartstudyai.data.entity.StudyActivity
import com.example.smartstudyai.data.entity.StudySchedule
import com.example.smartstudyai.data.entity.StudySession


@Database(

    entities = [
        StudyActivity::class,
        StudySchedule::class,
        StudySession::class
    ],
    version = 1
)

abstract class AppDatabase :
    RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun sessionDao(): SessionDao
}