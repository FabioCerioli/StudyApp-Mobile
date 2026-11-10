package com.example.smartstudyai.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smartstudyai.data.entity.StudySession


@Dao
interface SessionDao {

    @Insert
    suspend fun insert(
        session: StudySession
    ): Long

    @Update
    suspend fun update(
        session: StudySession
    )

    @Query("SELECT * FROM study_sessions WHERE activityId = :activityId ORDER BY id DESC")
    suspend fun getSessions(
        activityId: Long
    ): List<StudySession>

    @Query("SELECT COUNT(*) FROM study_sessions WHERE activityId = :activityId")
    suspend fun getSessionCount(
        activityId: Long
    ): Int

}