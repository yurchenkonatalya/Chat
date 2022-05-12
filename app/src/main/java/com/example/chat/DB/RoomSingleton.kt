package com.example.chat.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.DialogDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.DB.entity.AuthorizedUserEntity
import com.example.chat.DB.entity.DialogEntity
import com.example.chat.DB.entity.UserEntity


@Database(
    entities = arrayOf(UserEntity::class, AuthorizedUserEntity::class, DialogEntity::class),
    version = 1
)
abstract class RoomSingleton : RoomDatabase() {
    abstract fun roomUserDao(): UserDao
    abstract fun dialogDao(): DialogDao
    abstract fun roomAuthorizedUserDao(): AuthorizedUserDao
}
