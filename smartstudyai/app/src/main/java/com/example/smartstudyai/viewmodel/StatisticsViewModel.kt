package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstudyai.data.entity.StudySession
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ActivityStatistics(
    val totalSessions: Int = 0,
    val totalDurationSeconds: Long = 0,
    val averageConfidence: Float = 0f
)

class StatisticsViewModel(
    private val activityRepository: ActivityRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _activityTitle = MutableStateFlow("")
    val activityTitle: StateFlow<String> = _activityTitle
    private val _statistics = MutableStateFlow(ActivityStatistics())
    val statistics: StateFlow<ActivityStatistics> = _statistics
    private val _sessions = MutableStateFlow<List<StudySession>>(emptyList())

    val sessions: StateFlow<List<StudySession>> = _sessions

    fun loadStatistics(activityId: Long) {
        viewModelScope.launch {
            val activity = activityRepository.getActivity(activityId)
            _activityTitle.value = activity?.title ?: "Attività"
            val sessions = sessionRepository.getSessionsByActivity(activityId)
            _sessions.value = sessions

            if (sessions.isEmpty()) {
                _statistics.value = ActivityStatistics()
                return@launch
            }

            val totalDuration = sessions.sumOf { it.durationSeconds }

            val averageConfidence = sessions
                    .map { it.averageConfidence }
                    .average()
                    .toFloat()


            _statistics.value =
                ActivityStatistics(
                    totalSessions = sessions.size,
                    totalDurationSeconds = totalDuration,
                    averageConfidence = averageConfidence
                )
        }
    }
}