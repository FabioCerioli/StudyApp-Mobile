package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.SessionRepository

class StatisticsViewModelFactory(
    private val activityRepository: ActivityRepository,
    private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(
            activityRepository,
            sessionRepository
        ) as T
    }
}