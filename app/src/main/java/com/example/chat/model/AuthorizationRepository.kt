package com.example.chat.model

import android.util.Log
import com.example.chat.*
import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.DialogDao
import com.example.chat.DB.dao.LocaleDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.DB.entity.AuthorizedUserEntity
import com.example.chat.DB.entity.LocaleEntity
import com.example.chat.DB.entity.UserEntity
import com.example.chat.fragments.searchUsers.SearchUserSettings
import com.example.chat.network.api.user.UserApi
import com.example.chat.network.mappers.toUserEntity
import com.example.chat.network.responses.AuthorizeUserResponse
import com.example.chat.network.responses.StatusCodeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthorizationRepository(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val dialogDao: DialogDao,
    private val authorizedUserDao: AuthorizedUserDao,
    private val localeDao: LocaleDao
) {
    suspend fun updateLocale(locale: Int) {
        localeDao.deleteAll()
        localeDao.insert(LocaleEntity(locale))
    }

    suspend fun checkLocalAuthorizedUser(): UserEntity? {
        currentLocale = localeDao.get()?.locale ?: 0
        val authorizedEntity = authorizedUserDao.get() ?: return null
        val user = userDao.getById(authorizedEntity.id, true) ?: return null
        lateinit var response: Response<AuthorizeUserResponse>
        try {
            response = userApi.authorizeUser(user.user_nickname, user.user_password)
        } catch (e: Exception) {
            return null
        }
        if (!response.isSuccessful)
            return null
        val body = response.body() ?: return null
        if (body.id != null) {
            val entity = body.toUserEntity(user.user_password) ?: return null
            entity.isAuthorizeInfo = true
            userDao.insert(entity)
            AUTHORIZED_USER_PHOTO = entity.user_photo
            AUTHORIZED_USER_ID = entity.id
            return entity
        } else
            return null
    }

    suspend fun authorizeUser(login: String, password: String): UserEntity? {
        lateinit var response: Response<AuthorizeUserResponse>
        try {
            response = userApi.authorizeUser(login, password)
        } catch (e: Exception) {
            return null
        }
        if (!response.isSuccessful)
            return null
        val body = response.body() ?: return null
        if (body.id != null) {
            val entity = body.toUserEntity(password) ?: return null
            entity.isAuthorizeInfo = true
            userDao.insert(entity)
            AUTHORIZED_USER_PHOTO = entity.user_photo
            AUTHORIZED_USER_ID = entity.id
            authorizedUserDao.deleteAll()
            authorizedUserDao.insert(AuthorizedUserEntity(entity.id))
            return entity
        } else
            return null
    }

    suspend fun registerUser(
        login: String,
        password: String,
        name: String,
        surname: String,
        phone: String,
        mail: String,
        googleId: String,
        birthday: String,
        photo: String
    ): Int? {
        lateinit var response: Response<StatusCodeResponse>
        try {
            response = userApi.registerUser(
                login,
                password,
                name,
                surname,
                phone,
                mail,
                googleId,
                birthday,
                photo
            )
        } catch (e: Exception) {
            return null
        }
        if (!response.isSuccessful)
            return null
        val body = response.body() ?: return null
        if (body.status_code != null) {
            return body.status_code
        } else
            return null
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            authorizedUserDao.deleteAll()
        } catch (e: Exception) {
            Log.e("OXOXOXOXO", e.toString())
        }
    }

    suspend fun getAuthoresedUserInfo(): UserEntity? {
        try {
            val a = authorizedUserDao.get()
            Log.e("AAAAO", a.toString())
            return authorizedUserDao.get()?.let { userDao.getById(it.id, true) }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getUserById(id: Long): UserEntity? {
        try {
            val a = userDao.getById(id, false)
            Log.e("AAAAO", a.toString())
            return a
        } catch (e: Exception) {
            return null
        }
    }

    fun getUserListPagingSource() = userDao.pagingSource()
    suspend fun loadUserListData(settings: SearchUserSettings): Int {
        if (settings.lastLoadedPage == -1)
            userDao.deleteAllItems(false)
        val login = authorizedUserDao.get()
            ?.let {
                userDao.getById(it.id, true)?.user_nickname ?: return Constants.NO_CONNECTION_CODE
            } ?: return Constants.NO_CONNECTION_CODE
        val password = authorizedUserDao.get()
            ?.let {
                userDao.getById(it.id, true)?.user_password ?: return Constants.NO_CONNECTION_CODE
            } ?: return Constants.NO_CONNECTION_CODE
        val dataFromApi = QueryExecutor.getResponse {
            userApi.getUserList(login, password, settings.lastLoadedPage + 1, settings.search)
        } ?: return Constants.NO_CONNECTION_CODE
        if (dataFromApi.isEmpty())
            return Constants.NO_CONNECTION_CODE
        dataFromApi[0].status_code?.let {
            return it
        }
        if (dataFromApi[0].id == null)
            return Constants.NO_CONNECTION_CODE
        val offset = (settings.lastLoadedPage + 1) * Constants.PAGE_SIZE
        val list = mutableListOf<UserEntity>()
        for (index in dataFromApi.indices) {
            dataFromApi[index].toUserEntity("", false, offset + index)?.let {
                list.add(it)
            }
        }
        userDao.insertList(list)
        return Constants.STATUS_CODE_SUCCESS
    }

    suspend fun updateOnline() {
        val login = authorizedUserDao.get()
            ?.let { userDao.getById(it.id, true)?.user_nickname ?: return } ?: return
        val password = authorizedUserDao.get()
            ?.let { userDao.getById(it.id, true)?.user_password ?: return } ?: return
        QueryExecutor.getResponse {
            userApi.updateUserOnline(login, password)
        }
    }

    val lastOnlineUsers: MutableList<UserEntity> = mutableListOf()
    suspend fun getUsersInOnline() {
        val login = authorizedUserDao.get()
            ?.let { userDao.getById(it.id, true)?.user_nickname ?: return } ?: return
        val password = authorizedUserDao.get()
            ?.let { userDao.getById(it.id, true)?.user_password ?: return } ?: return
        val dataFromApi = QueryExecutor.getResponse {
            userApi.getUsersInOnline(login, password)
        } ?: return
        if (dataFromApi.isEmpty())
            return
        dataFromApi[0].status_code?.let {
            return
        }
        if (dataFromApi[0].id == null)
            return
        for (item in lastOnlineUsers) {
            userDao.updateOnline(item.id, "1970-12-12 00:00:00")
            dialogDao.updateOnline(item.id, "1970-12-12 00:00:00")
        }
        lastOnlineUsers.clear()
        for (index in dataFromApi.indices) {
            val item = dataFromApi[index].toUserEntity("", false, 1)
            item?.let {
                lastOnlineUsers.add(item)
                userDao.updateOnline(it.id, it.user_last_active)
                dialogDao.updateOnline(it.id, it.user_last_active)
            }
        }
    }

    suspend fun changeUserPhoto(photo: String): Int {
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
        QueryExecutor.getResponse {
            userApi.changeUserPhoto(login, password, photo)
        } ?: return Constants.NO_CONNECTION_CODE
        return 0
    }
}