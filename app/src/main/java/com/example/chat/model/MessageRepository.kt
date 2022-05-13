package com.example.chat.model

import com.example.chat.Constants
import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.DialogDao
import com.example.chat.DB.dao.MessageDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.DB.entity.DialogEntity
import com.example.chat.DB.entity.MessageEntity
import com.example.chat.QueryExecutor
import com.example.chat.fragments.searchUsers.SearchUserSettings
import com.example.chat.network.api.user.MessageApi
import com.example.chat.network.mappers.toDialogEntity
import com.example.chat.network.mappers.toMessageEntity

class MessageRepository(
    private val messageApi: MessageApi,
    private val userDao: UserDao,
    private val authorizedUserDao: AuthorizedUserDao,
    private val dialogDao: DialogDao,
    private val messageDao: MessageDao
) {
    fun getDialogListPagingSource() = dialogDao.pagingSource()
    fun getMessageListPagingSource() = messageDao.pagingSource()
    suspend fun getMaxDialogId() = dialogDao.getMax()
    suspend fun getMaxMessageId() = messageDao.getMax()
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
            messageApi.getDialogList(login, password, settings.lastLoadedPage + 1, settings.search)
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

    suspend fun regularUpdateData(settings: SearchUserSettings, startId: Long): Int {
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
            messageApi.getLastDialogList(login, password, startId, settings.search)
        } ?: return Constants.NO_CONNECTION_CODE
        if (dataFromApi.isEmpty())
            return Constants.NO_CONNECTION_CODE
        dataFromApi[0].status_code?.let {
            return it
        }
        if (dataFromApi[0].message_id == null)
            return Constants.NO_CONNECTION_CODE
        if (settings.lastLoadedPage == -1) {
            return Constants.WRONG_TOKEN_EXCEPTION
        }
        val list = mutableListOf<DialogEntity>()
        for (index in dataFromApi.indices) {
            dataFromApi[index].toDialogEntity(authorizedId, 1)?.let {
                list.add(it)
            }
        }
        dialogDao.insertList(list)
        return Constants.STATUS_CODE_SUCCESS
    }

    suspend fun loadMessageListData(settings: SearchUserSettings, id_user: Long): Int {
        if (settings.lastLoadedPage == -1)
            messageDao.deleteAllItems()
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
            messageApi.getMessageList(login, password, settings.lastLoadedPage + 1, id_user)
        } ?: return Constants.NO_CONNECTION_CODE
        if (dataFromApi.isEmpty())
            return Constants.NO_CONNECTION_CODE
        dataFromApi[0].status_code?.let {
            return it
        }
        if (dataFromApi[0].id == null)
            return Constants.NO_CONNECTION_CODE
        val list = mutableListOf<MessageEntity>()
        for (index in dataFromApi.indices) {
            dataFromApi[index].toMessageEntity()?.let {
                list.add(it)
            }
        }
        messageDao.insertList(list)
        return Constants.STATUS_CODE_SUCCESS
    }

    suspend fun regularUpdateMessageData(
        settings: SearchUserSettings,
        id_user: Long,
        startId: Long
    ): Int {
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
            messageApi.getLastMessageList(login, password, startId, id_user)
        } ?: return Constants.NO_CONNECTION_CODE
        if (dataFromApi.isEmpty())
            return Constants.NO_CONNECTION_CODE
        dataFromApi[0].status_code?.let {
            return it
        }
        if (dataFromApi[0].id == null)
            return Constants.NO_CONNECTION_CODE
        if (settings.lastLoadedPage == -1) {
            return Constants.WRONG_TOKEN_EXCEPTION
        }
        val list = mutableListOf<MessageEntity>()
        for (index in dataFromApi.indices) {
            dataFromApi[index].toMessageEntity()?.let {
                list.add(it)
            }
        }
        messageDao.insertList(list)
        return Constants.STATUS_CODE_SUCCESS
    }

    suspend fun sendMessage(id_user: Long, text: String): Int {
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
            messageApi.sendMessage(login, password, id_user, text)
        } ?: return Constants.NO_CONNECTION_CODE
        dataFromApi.status_code?.let {
            return it
        }
        return Constants.NO_CONNECTION_CODE
    }

    suspend fun sendMessagePhoto(id_user: Long, photo: String): Int {
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
            messageApi.sendMessagePhoto(login, password, id_user, photo)
        } ?: return Constants.NO_CONNECTION_CODE
        dataFromApi.status_code?.let {
            return it
        }
        return Constants.NO_CONNECTION_CODE
    }

    suspend fun getOnlineById(id: Long): String? {
        return dialogDao.getByOpponentId(id)?.opponent_last_active
    }
}