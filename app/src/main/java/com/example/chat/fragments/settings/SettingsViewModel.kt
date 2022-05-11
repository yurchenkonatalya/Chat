package com.example.chat.fragments.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.DB.entity.UserEntity
import com.example.chat.fragments.signin.SigninViewModel
import com.example.chat.model.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel() {
    val userFlow = MutableStateFlow<UserEntity?>(null)

    init{
        getAuthoresedUserInfo()
    }

    fun logout() = viewModelScope.launch {
        authorizationRepository.logout()
//         withContext(Dispatchers.IO) {
//             findNavController().popBackStack(R.id.nav_signin_fragment, false)
//         }
    }

    fun getAuthoresedUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            val res = authorizationRepository.getAuthoresedUserInfo()
            userFlow.emit(res)
        }
    }
}
