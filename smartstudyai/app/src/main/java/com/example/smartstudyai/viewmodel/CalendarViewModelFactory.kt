package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.ScheduleRepository

class CalendarViewModelFactory(
    private val activityRepository: ActivityRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (
            modelClass.isAssignableFrom(
                CalendarViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(
                activityRepository,
                scheduleRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}