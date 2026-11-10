package com.example.smartstudyai.data.repository


import com.example.smartstudyai.data.dao.SessionDao
import com.example.smartstudyai.data.entity.StudySession


class SessionRepository(
    private val dao: SessionDao
) {
    suspend fun insert(session: StudySession): Long {
        return dao.insert(session)
    }

    suspend fun update(session: StudySession){
        dao.update(session)
    }

    suspend fun getSessionsByActivity(activityId: Long): List<StudySession> {
        return dao.getSessions(activityId)
    }
}