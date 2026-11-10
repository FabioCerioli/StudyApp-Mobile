package com.example.smartstudyai.data.api


import com.example.smartstudyai.data.model.PredictionResponse
import com.example.smartstudyai.data.model.WindowData
import retrofit2.http.Body
import retrofit2.http.POST



interface SensorApi {
    @POST("predict")
    suspend fun predict(
        @Body window: WindowData
    ): PredictionResponse
}