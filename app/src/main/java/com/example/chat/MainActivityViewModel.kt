package com.example.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel() {
    private var job: Job? = null
    private var job2: Job? = null
    fun onViewAttach() {
        job = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                authorizationRepository.updateOnline()
                delay(5000)
            }
        }
        job2 = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                authorizationRepository.getUsersInOnline()
                delay(5000)
            }
        }
    }

    fun onViewStop() {
        job?.cancel()
        job = null
        job2?.cancel()
        job2 = null
    }
}