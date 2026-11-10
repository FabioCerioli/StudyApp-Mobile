package com.example.smartstudyai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smartstudyai.data.entity.StudyActivity
import kotlinx.coroutines.flow.Flow
@Dao
interface ActivityDao {

    @Insert
    suspend fun insert(
        activity: StudyActivity
    ): Long

    @Update
    suspend fun update(
        activity: StudyActivity
    )

    @Delete
    suspend fun delete(
        activity: StudyActivity
    )

    @Query(
        "SELECT * FROM study_activity ORDER BY deadline"
    )
    fun getAll(): Flow<List<StudyActivity>>

    @Query(
        "SELECT * FROM study_activity WHERE activityId = :id "
    )
    suspend fun getById(
        id: Long
    ): StudyActivity?

}