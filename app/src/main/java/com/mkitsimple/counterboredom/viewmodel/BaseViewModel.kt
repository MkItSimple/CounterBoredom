package com.example.dkt2.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {

    private lateinit var job: Job

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}