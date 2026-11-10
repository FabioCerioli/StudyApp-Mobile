package com.example.smartstudyai.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "study_activity")
data class StudyActivity(
    @PrimaryKey(autoGenerate = true)

    val activityId: Long = 0,
    val title: String,
    val description: String,
    val weeklyHours: Float,
    val deadline: Long
)