package com.example.smartstudyai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_schedule",
    foreignKeys = [
        ForeignKey(
            entity = StudyActivity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StudySchedule(
    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,
    val activityId: Long,
    val day: String,
    val startTime: String,
    val endTime: String
)