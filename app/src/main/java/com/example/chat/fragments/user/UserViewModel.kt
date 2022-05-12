package com.example.chat.fragments.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.DB.entity.UserEntity
import com.example.chat.model.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val arguments: SavedStateHandle
) : ViewModel() {
    val loadingStatus = MutableStateFlow(0)
    var userInfo: UserEntity? = null
    private var isInit = false
    fun onViewAttached(requireRestore: Boolean) {
        if (!isInit) {
            isInit = true
            getUserInfo(!requireRestore)
        }
    }

    private fun getUserInfo(requireUpdate: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = arguments.get<Long>("userId") ?: return@launch
            loadingStatus.value = 1
            val userResponse = authorizationRepository.getUserById(userId)
            if (userResponse != null) {
                userInfo = userResponse
                loadingStatus.value = 0
            } else
                loadingStatus.value = 2
        }
    }
}
