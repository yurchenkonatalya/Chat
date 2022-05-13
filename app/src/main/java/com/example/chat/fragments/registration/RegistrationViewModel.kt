package com.example.chat.fragments.registration

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.Constants
import com.example.chat.ImageHelper
import com.example.chat.model.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val imageHelper: ImageHelper
) : ViewModel() {
    enum class States {
        NORMAL,
        LOADING,
        ERROR,
        LOGIN_EXIST,
        SUCCESS
    }

    val registratedResultFlow = MutableStateFlow<States>(States.LOADING)
    fun registerUser(
        login: String,
        password: String,
        name: String,
        surname: String,
        phone: String,
        mail: String,
        googleId: String,
        birthday: String,
        photo: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            registratedResultFlow.emit(States.LOADING)
            val statusCode = authorizationRepository.registerUser(
                login,
                password,
                name,
                surname,
                phone,
                mail,
                googleId,
                birthday,
                imageHelper.bitmapToBase64(photo, true)
            )
            when {
                statusCode == Constants.LOGIN_EXIST_EXCEPTION -> registratedResultFlow.emit(States.LOGIN_EXIST)
                statusCode != 0 -> registratedResultFlow.emit(States.ERROR)
                else -> registratedResultFlow.emit(States.SUCCESS)
            }
        }
    }
}
