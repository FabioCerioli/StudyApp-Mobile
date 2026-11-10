package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstudyai.data.repository.ActivityRepository

class ActivityViewModelFactory(
    private val repository: ActivityRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActivityViewModel(repository) as T
    }

}