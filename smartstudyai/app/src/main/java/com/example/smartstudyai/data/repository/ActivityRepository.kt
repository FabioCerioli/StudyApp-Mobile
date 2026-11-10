package com.example.smartstudyai.data.repository

import com.example.smartstudyai.data.dao.ActivityDao
import com.example.smartstudyai.data.entity.StudyActivity

class ActivityRepository(
    private val dao: ActivityDao
) {

    fun getAllActivities() = dao.getAll()

    suspend fun insert(activity: StudyActivity): Long {
        return dao.insert(activity)
    }
    suspend fun getActivity(id: Long): StudyActivity? {
        return dao.getById(id)
    }

    suspend fun update(activity: StudyActivity) {
        dao.update(activity)
    }

    suspend fun delete(activity: StudyActivity) {
        dao.delete(activity)
    }
}