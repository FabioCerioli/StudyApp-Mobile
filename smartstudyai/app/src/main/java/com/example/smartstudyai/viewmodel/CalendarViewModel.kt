package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smartstudyai.data.entity.StudyActivity
import com.example.smartstudyai.data.entity.StudySchedule
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class CalendarSchedule(
    val schedule: StudySchedule,
    val activity: StudyActivity?
)

class CalendarViewModel(
    activityRepository: ActivityRepository,
    scheduleRepository: ScheduleRepository
) : ViewModel() {

    val schedules =
        combine(
            activityRepository.getAllActivities(),
            scheduleRepository.getAllSchedules()
        ) { activities, schedules ->
            schedules.map { schedule ->
                CalendarSchedule(
                    schedule = schedule,
                    activity = activities.find { it.activityId == schedule.activityId }
                )
            }
        }
}