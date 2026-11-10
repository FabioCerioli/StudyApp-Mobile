package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smartstudyai.data.entity.StudySession
import com.example.smartstudyai.data.repository.SensorRepository
import com.example.smartstudyai.data.repository.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudySessionViewModel(
    private val sensorRepository: SensorRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val prediction = sensorRepository.prediction
    val confidence = sensorRepository.confidence
    val connectionError = sensorRepository.connectionError
    val hasPrediction = sensorRepository.hasPrediction
    private var currentSessionId: Long? = null
    private var currentActivityId: Long = 0L
    private var sessionStartTime: Long = 0L
    private val _isStudying = MutableStateFlow(false)
    val isStudying: StateFlow<Boolean> = _isStudying
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var timerJob: Job? = null
    fun startSession(activityId: Long) {
        currentActivityId = activityId
        _elapsedTime.value = 0L
        currentSessionId = null
        sessionStartTime = System.currentTimeMillis()
        coroutineScope.launch {
            val session =
                StudySession(
                    activityId = activityId,
                    startTime = sessionStartTime
                )
            currentSessionId = sessionRepository.insert(session)
        }
        _isStudying.value = true
        startTimer()
        startSensors()
    }
    fun stopSession() {
        timerJob?.cancel()
        stopSensors()
        _isStudying.value = false
        coroutineScope.launch {
            val id = currentSessionId ?: return@launch
            val session =
                StudySession(
                    id = id,
                    activityId = currentActivityId,
                    startTime = sessionStartTime,
                    endTime = System.currentTimeMillis(),
                    durationSeconds = elapsedTime.value,
                    averageConfidence = confidence.value.toFloat(),
                    prediction = prediction.value
                )
            sessionRepository.update(session)
        }
    }
    private fun startTimer() {
        timerJob?.cancel()
        timerJob =
            coroutineScope.launch {
                while (true) {
                    delay(1000)
                    _elapsedTime.value++
                }
            }
    }
    private fun startSensors() {
        sensorRepository.startCollecting()
    }
    private fun stopSensors() {
        sensorRepository.stopCollecting()
    }
    override fun onCleared() {
        sensorRepository.stopCollecting()
        timerJob?.cancel()
        coroutineScope.cancel()
    }
}
