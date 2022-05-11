package com.example.chat.DI

import android.content.Context
import androidx.room.Room
import com.example.chat.DB.RoomSingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext applicationContext: Context) : RoomSingleton =
        Room.databaseBuilder(
            applicationContext,
            RoomSingleton::class.java,
            "RoomDB"
        ).build()

    @Provides
    @Singleton
    fun provideRoomUserDao(database : RoomSingleton) = database.roomUserDao()

    @Provides
    @Singleton
    fun provideRoomAuthorizedUserDao(database : RoomSingleton) = database.roomAuthorizedUserDao()
}