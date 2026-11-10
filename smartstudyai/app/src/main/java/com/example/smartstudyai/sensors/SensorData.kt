package com.example.smartstudyai.sensors


data class SensorData(
    val timestamp: Long,

    val ax: Float,
    val ay: Float,
    val az: Float,

    val gx: Float,
    val gy: Float,
    val gz: Float
)