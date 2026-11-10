package com.example.smartstudyai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = StudyActivity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StudySession(
    @PrimaryKey(autoGenerate = true)

    val id: Long = 0,
    val activityId: Long,
    val startTime: Long,
    val endTime: Long? = null,
    val durationSeconds: Long = 0,
    val averageConfidence: Float = 0f,
    val prediction: String = "UNKNOWN"
)