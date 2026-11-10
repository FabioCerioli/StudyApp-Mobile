package com.example.smartstudyai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstudyai.data.repository.ActivityRepository


class SessionActivityViewModelFactory( private val repository: ActivityRepository ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create( modelClass: Class<T> ): T {

        if(modelClass.isAssignableFrom(
                SessionActivityViewModel::class.java
            )){
            return SessionActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}