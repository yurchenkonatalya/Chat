package com.example.chat.model

import android.util.Log
import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.DB.entity.AuthorizedUserEntity
import com.example.chat.DB.entity.UserEntity
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
    private val authorizedUserDao: AuthorizedUserDao
) {

    suspend fun checkLocalAuthorizedUser(): UserEntity? {
        val authorizedEntity = authorizedUserDao.get() ?: return null

        val user = userDao.getById(authorizedEntity.id) ?: return null

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

            userDao.insert(entity)

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
            userDao.insert(entity)

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
            return authorizedUserDao.get()?.let { userDao.getById(it.id) }
        }catch (e:Exception){
            return null
        }
    }
}
