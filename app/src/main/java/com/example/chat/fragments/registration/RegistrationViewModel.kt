package com.example.chat.fragments.registration

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.ImageHelper
import com.example.chat.fragments.signin.SigninViewModel
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
    enum class States{
        NORMAL,
        LOADING,
        ERROR,
        SUCCESS
    }

    val registratedResultFlow = MutableStateFlow<States>(States.LOADING)
    val loadingStatus = MutableStateFlow(0)


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
            Log.e("STATUSCODE", statusCode.toString())
            if (statusCode != 0)
                registratedResultFlow.emit(States.ERROR)
            else
                registratedResultFlow.emit(States.SUCCESS)
        }
    }
    suspend fun onBitmapChanged(){
       // imageHelper.saveToStorage(bitmap.value?: return, "registration_photo")
    }
}
