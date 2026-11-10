package com.example.smartstudyai.data.repository

import com.example.smartstudyai.data.api.ApiClient
import com.example.smartstudyai.data.model.WindowData
import com.example.smartstudyai.sensors.SensorCollector
import com.example.smartstudyai.sensors.SensorData
import com.example.smartstudyai.sensors.WindowBuffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SensorRepository(
    private val sensorCollector: SensorCollector
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _sensorData = MutableStateFlow<SensorData?>(null)

    val sensorData: StateFlow<SensorData?> = _sensorData

    private val _prediction = MutableStateFlow("N/A")

    val prediction: StateFlow<String> = _prediction

    private val _confidence = MutableStateFlow(0.0)

    val confidence: StateFlow<Double> = _confidence

    private val _connectionError = MutableStateFlow(false)

    val connectionError: StateFlow<Boolean> = _connectionError

    private val _hasPrediction = MutableStateFlow(false)

    val hasPrediction: StateFlow<Boolean> = _hasPrediction

    private val windowBuffer = WindowBuffer()

    private val _windowData = MutableStateFlow<WindowData?>(null)

    private val api = ApiClient.sensorApi

    fun startCollecting() {

        _hasPrediction.value = false
        _connectionError.value = false

        sensorCollector.start { data ->

            _sensorData.value = data

            val window = windowBuffer.addSample(data)

            if (window != null) {

                val windowData = WindowData(window)
                _windowData.value = windowData

                scope.launch {

                    try {

                        val response = api.predict(windowData)
                        _connectionError.value = false
                        _hasPrediction.value = true
                        _prediction.value = response.prediction
                        _confidence.value = response.confidence

                    } catch (e: Exception) {
                        _connectionError.value = true
                    }
                }
            }
        }
    }

    fun stopCollecting() {
        sensorCollector.stop()
    }


}
