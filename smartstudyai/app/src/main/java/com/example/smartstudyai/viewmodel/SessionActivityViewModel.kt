package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstudyai.data.entity.StudyActivity
import com.example.smartstudyai.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SessionActivityViewModel( private val repository: ActivityRepository ): ViewModel(){

    private val _activity = MutableStateFlow<StudyActivity?>(null)
    val activity: StateFlow<StudyActivity?> = _activity

    fun loadActivity(id: Long){
        viewModelScope.launch { _activity.value = repository.getActivity(id) }
    }
}