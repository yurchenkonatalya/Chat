package com.example.chat.fragments.dialog

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.chat.Constants
import com.example.chat.fragments.searchUsers.SearchUserSettings
import com.example.chat.model.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private var arguments: SavedStateHandle
) : ViewModel() {
    var settings = SearchUserSettings()
    val userId = arguments.get<Long>("userId") ?: -1L
    val lastActiveFlow = MutableStateFlow<String?>(null)
    val data = Pager(
        PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            maxSize = Constants.MAX_SIZE
        )
    ) {
        messageRepository.getMessageListPagingSource()
    }.flow.cachedIn(viewModelScope)

    //0 - загрузки нет
    //1 - подгружается новая страница, хотя бы одна уже загружена до этого
    //2 - ошибка загрузки, хотя бы одна страница уже загружена
    //3 - загружается первая страница
    //4 - ошибка загрузки первой страницы
    //5 - пустой последний результат
    val loadingStatus = MutableStateFlow(0)
    private var loadingJob: Job? = null
    private var liveDataJob: Job? = null
    private var onlineJob: Job? = null
    private var inited = false
    private var lastMessageId: Long? = null
    private fun loadData(onRefresh: Boolean = false, saveFilter: Boolean = true) {
        if (onRefresh) {
            loadingJob?.cancel()
            loadingStatus.value = 3
            settings = SearchUserSettings()
        } else {
            if (settings.endOfPagination ||
                (loadingStatus.value != 0)
            )
                return
            loadingStatus.value = 1
        }
        loadingJob = viewModelScope.launch(Dispatchers.IO) {
            val res = messageRepository.loadMessageListData(settings, userId)
            Log.e("RESSS", res.toString())
            if (!isActive)
                return@launch
            when (res) {
                Constants.STATUS_CODE_SUCCESS -> {
                    settings.lastLoadedPage++
                    lastMessageId = messageRepository.getMaxMessageId()
                    loadingStatus.value = 0
                    if (settings.lastLoadedPage == 0)
                        loadData(onRefresh = false, saveFilter = true)
                }
                Constants.STATUS_CODE_EMPTY -> {
                    settings.endOfPagination = true
                    if (loadingStatus.value == 1)
                        loadingStatus.value = if (settings.lastLoadedPage == -1) 5 else 0
                    else
                        loadingStatus.value = 0
                }
                else -> {
                    if (loadingStatus.value == 1) {
                        loadingStatus.value = 2
                    } else
                        loadingStatus.value = 4
                }
            }
        }
    }

    fun onRefresh() {
        loadData(onRefresh = true, saveFilter = true)
    }

    fun onListEnded() {
        loadData()
    }

    fun onViewAttach(requireRestore: Boolean) {
        val restore = requireRestore || inited
        if (!restore || (!inited && settings.lastLoadedPage == -1))
            loadData(onRefresh = true, saveFilter = restore)
        inited = true
    }

    fun onViewStart() {
        lastMessageId = -1
        liveDataJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val lastActive = messageRepository.getOnlineById(userId)
                Log.e("NEWER", lastActive.toString())
                lastActiveFlow.emit(null)
                lastActiveFlow.emit(lastActive)
                delay(2000)
            }
        }

        onlineJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                lastMessageId?.let {
                    if (loadingStatus.value != 1 && loadingStatus.value != 3) {
                        val code = messageRepository.regularUpdateMessageData(settings, userId, it)
                        if (code == Constants.WRONG_TOKEN_EXCEPTION) {
                            onRefresh()
                        }
                        lastMessageId = messageRepository.getMaxMessageId() ?: -1L
                    }
                }
                delay(2000)
            }
        }
    }

    fun onViewStop(position: Int, offset: Int) {
        settings.position = position
        settings.offset = offset
        liveDataJob?.cancel()
        onlineJob?.cancel()
    }

    fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(userId, text)
        }
    }

    fun sendMessagePhoto(photo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessagePhoto(userId, photo)
        }
    }
}