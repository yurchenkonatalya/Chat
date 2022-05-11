package com.example.chat.fragments.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.DI.NetworkModule
import com.example.chat.model.AuthorizationRepository
import com.example.chat.network.api.user.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel() {

    enum class States{
        NORMAL,
        LOADING,
        ERROR,
        SUCCESS
    }

    val authorizedResultFlow = MutableStateFlow<States>(States.LOADING)

    init{
        viewModelScope.launch(Dispatchers.IO) {
            val entity = authorizationRepository.checkLocalAuthorizedUser()

            if(entity != null)
                authorizedResultFlow.emit(States.SUCCESS)
            else
                authorizedResultFlow.emit(States.NORMAL)
        }
    }

    fun authorizeUser(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO){
            authorizedResultFlow.emit(States.LOADING)

            val entity = authorizationRepository.authorizeUser(login ,password)

            if(entity == null)
                authorizedResultFlow.emit(States.ERROR)
            else
                authorizedResultFlow.emit(States.SUCCESS)
        }
    }
}