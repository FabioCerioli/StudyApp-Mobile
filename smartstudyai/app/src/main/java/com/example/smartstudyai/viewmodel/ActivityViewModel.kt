package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstudyai.data.entity.StudyActivity
import com.example.smartstudyai.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ActivityViewModel(
    private val repository: ActivityRepository
) : ViewModel() {

    val activities = repository.getAllActivities()
    private val _selectedActivity = MutableStateFlow<StudyActivity?>(null)
    val selectedActivity = _selectedActivity
    fun addActivity(
        title: String,
        description: String,
        weeklyHours: Float,
        deadline: Long,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val activityId = repository.insert(
                StudyActivity(
                    title = title,
                    description = description,
                    weeklyHours = weeklyHours,
                    deadline = deadline
                )
            )
            onComplete(activityId)
        }
    }


    fun loadActivity(activityId: Long){
        viewModelScope.launch { _selectedActivity.value = repository.getActivity(activityId) }
    }
    fun updateActivity(activity: StudyActivity){
        viewModelScope.launch { repository.update(activity) }
    }
    fun deleteActivity(activity: StudyActivity) {
        viewModelScope.launch { repository.delete(activity) }
    }
}