package com.example.smartstudyai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smartstudyai.data.entity.StudySchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    suspend fun insert(
        schedule: StudySchedule
    )

    @Update
    suspend fun update(
        schedule: StudySchedule
    )

    @Delete
    suspend fun delete(
        schedule: StudySchedule
    )

    @Query("SELECT * FROM study_schedule WHERE activityId = :activityId")
    suspend fun getSchedulesForActivity(
        activityId: Long
    ): List<StudySchedule>

    @Query("DELETE FROM study_schedule WHERE activityId = :activityId")
    suspend fun deleteSchedulesForActivity(
        activityId: Long
    )

    @Query("SELECT * FROM study_schedule ORDER BY day, startTime")
    fun getAllSchedules(): Flow<List<StudySchedule>>
}