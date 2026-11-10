package com.example.smartstudyai.data.model


data class PredictionResponse(

    val prediction: String,
    val confidence: Double,
    val features: Int
)