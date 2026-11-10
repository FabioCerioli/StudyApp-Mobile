package com.example.smartstudyai.sensors

class WindowBuffer(private val windowSize: Int = 128) {
    private val buffer = mutableListOf<SensorData>()

    fun addSample(sample: SensorData): List<SensorData>?
    {
        buffer.add(sample)

        return if (buffer.size >= windowSize) {
            val window = buffer.toList()
            buffer.clear()
            window
        } else { null }
    }
}