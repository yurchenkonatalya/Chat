package com.example.chat.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chat.DB.dao.*
import com.example.chat.DB.entity.*

@Database(
    entities = [UserEntity::class, AuthorizedUserEntity::class, DialogEntity::class, MessageEntity::class, LocaleEntity::class],
    version = 1
)
abstract class RoomSingleton : RoomDatabase() {
    abstract fun roomUserDao(): UserDao
    abstract fun dialogDao(): DialogDao
    abstract fun messageDao(): MessageDao
    abstract fun roomAuthorizedUserDao(): AuthorizedUserDao
    abstract fun localeDao(): LocaleDao
}
