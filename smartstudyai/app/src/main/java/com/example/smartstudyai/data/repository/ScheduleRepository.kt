package com.example.smartstudyai.data.repository

import com.example.smartstudyai.data.dao.ScheduleDao
import com.example.smartstudyai.data.entity.StudySchedule

class ScheduleRepository(private val dao: ScheduleDao) {
    suspend fun insert(schedule: StudySchedule) {
        dao.insert(schedule)
    }

    suspend fun getSchedules(activityId: Long): List<StudySchedule> {
        return dao.getSchedulesForActivity(activityId)
    }

    fun getAllSchedules() = dao.getAllSchedules()

    suspend fun deleteSchedules(activityId: Long) {
        dao.deleteSchedulesForActivity(activityId)
    }

    suspend fun replaceSchedules(activityId: Long, schedules: List<StudySchedule>) {

        dao.deleteSchedulesForActivity(activityId)
        schedules.forEach { schedule ->
            dao.insert(schedule)
        }
    }

}