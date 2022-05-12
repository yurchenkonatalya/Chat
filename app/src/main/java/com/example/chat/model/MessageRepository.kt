package com.example.chat.model

import com.example.chat.Constants
import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.DialogDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.DB.entity.DialogEntity
import com.example.chat.QueryExecutor
import com.example.chat.fragments.searchUsers.SearchUserSettings
import com.example.chat.network.api.user.MessageApi
import com.example.chat.network.mappers.toDialogEntity

class MessageRepository(
    private val messageApi: MessageApi,
    private val userDao: UserDao,
    private val authorizedUserDao: AuthorizedUserDao,
    private val dialogDao: DialogDao
) {
    fun getDialogListPagingSource() = dialogDao.pagingSource()
    suspend fun loadDialogListData(settings: SearchUserSettings): Int {
        if (settings.lastLoadedPage == -1)
            dialogDao.deleteAllItems()
        var authorizedId = -1L
        val login = authorizedUserDao.get()
            ?.let {
                authorizedId = it.id; userDao.getById(it.id, true)?.user_nickname
                ?: return Constants.NO_CONNECTION_CODE
            } ?: return Constants.NO_CONNECTION_CODE
        val password = authorizedUserDao.get()
            ?.let {
                userDao.getById(it.id, true)?.user_password ?: return Constants.NO_CONNECTION_CODE
            } ?: return Constants.NO_CONNECTION_CODE
        val dataFromApi = QueryExecutor.getResponse {
            messageApi.getUserList(login, password, settings.lastLoadedPage + 1, settings.search)
        } ?: return Constants.NO_CONNECTION_CODE
        if (dataFromApi.isEmpty())
            return Constants.NO_CONNECTION_CODE
        dataFromApi[0].status_code?.let {
            return it
        }
        if (dataFromApi[0].message_id == null)
            return Constants.NO_CONNECTION_CODE
        val offset = (settings.lastLoadedPage + 1) * Constants.PAGE_SIZE
        val list = mutableListOf<DialogEntity>()
        for (index in dataFromApi.indices) {
            dataFromApi[index].toDialogEntity(authorizedId, offset + index)?.let {
                list.add(it)
            }
        }
        dialogDao.insertList(list)
        return Constants.STATUS_CODE_SUCCESS
    }
}