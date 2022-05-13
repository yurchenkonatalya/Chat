package com.example.chat.fragments.searchUsers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.chat.Constants
import com.example.chat.model.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel() {
    var settings = SearchUserSettings()
    val data = Pager(
        PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            maxSize = Constants.MAX_SIZE
        )
    ) {
        authorizationRepository.getUserListPagingSource()
    }.flow.cachedIn(viewModelScope)

    //0 - загрузки нет
    //1 - подгружается новая страница, хотя бы одна уже загружена до этого
    //2 - ошибка загрузки, хотя бы одна страница уже загружена
    //3 - загружается первая страница
    //4 - ошибка загрузки первой страницы
    //5 - пустой последний результат
    val loadingStatus = MutableStateFlow(0)
    private var loadingJob: Job? = null
    private var searchQueryChangeJob: Job? = null
    private var inited = false
    private fun loadData(onRefresh: Boolean = false, saveFilter: Boolean = true) {
        if (onRefresh) {
            loadingJob?.cancel()
            loadingStatus.value = 3
            if (saveFilter) {
                val search = settings.search
                settings = SearchUserSettings()
                settings.search = search
            } else
                settings = SearchUserSettings()
        } else {
            if (settings.endOfPagination ||
                (loadingStatus.value != 0)
            )
                return
            loadingStatus.value = 1
        }
        loadingJob = viewModelScope.launch(Dispatchers.IO) {
            val res = authorizationRepository.loadUserListData(settings)
            if (!isActive)
                return@launch
            when (res) {
                Constants.STATUS_CODE_SUCCESS -> {
                    settings.lastLoadedPage++
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

    fun onSearchStateChanged(search: String) {
        searchQueryChangeJob?.cancel()
        searchQueryChangeJob = viewModelScope.launch(Dispatchers.Default) {
            delay(400)
            if (isActive) {
                settings.search = search
                onRefresh()
            }
        }
    }

    fun onViewAttach(requireRestore: Boolean) {
        val restore = requireRestore || inited
        if (!restore || (!inited && settings.lastLoadedPage == -1))
            loadData(onRefresh = true, saveFilter = restore)
        inited = true
    }

    fun onViewStop(position: Int, offset: Int) {
        settings.position = position
        settings.offset = offset
    }
}