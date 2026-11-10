package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstudyai.data.repository.SensorRepository
import com.example.smartstudyai.data.repository.SessionRepository


class StudySessionViewModelFactory(
    private val sensorRepository: SensorRepository,
    private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StudySessionViewModel(
            sensorRepository,
            sessionRepository
        ) as T
    }
}