package com.example.chat.network.mappers

import com.example.chat.DB.entity.AuthorizedUserEntity
import com.example.chat.DB.entity.UserEntity
import com.example.chat.network.responses.AuthorizeUserResponse

fun AuthorizeUserResponse.toUserEntity(password: String): UserEntity?{
    return UserEntity(
        id?:return null,
        name?:return null,
        surname?:return null,
        mail?: return null,
        phone?: return null,
        nickname?: return null,
        password,
        birthday ?: return null,
        lastActive?: return null,
        photo ?: return null
    )
}