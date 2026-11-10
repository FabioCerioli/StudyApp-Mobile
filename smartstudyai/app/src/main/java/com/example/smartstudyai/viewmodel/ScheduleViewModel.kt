package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstudyai.data.entity.StudySchedule
import com.example.smartstudyai.data.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel( private val repository: ScheduleRepository ) : ViewModel() {

    fun replaceSchedules(
        activityId: Long,
        schedules: List<StudySchedule>,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.replaceSchedules(
                activityId = activityId,
                schedules = schedules
            )
            onComplete?.invoke()
        }
    }
    fun saveSchedules(
        activityId: Long,
        schedules: List<StudySchedule>,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.replaceSchedules(
                activityId = activityId,
                schedules = schedules
            )
            onComplete?.invoke()
        }
    }
}