package com.example.chat.DI

import com.example.chat.DB.dao.*
import com.example.chat.model.AuthorizationRepository
import com.example.chat.model.MessageRepository
import com.example.chat.network.api.user.MessageApi
import com.example.chat.network.api.user.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @ActivityRetainedScoped
    fun provideAuthorizationRepository(
        userApi: UserApi,
        userDao: UserDao,
        authorizedUserDao: AuthorizedUserDao,
        dialogDao: DialogDao,
        localeDao: LocaleDao
    ) = AuthorizationRepository(userApi, userDao, dialogDao, authorizedUserDao, localeDao)

    @Provides
    @ActivityRetainedScoped
    fun provideMessageRepository(
        messageApi: MessageApi,
        userDao: UserDao,
        authorizedUserDao: AuthorizedUserDao,
        dialogDao: DialogDao,
        messageDao: MessageDao
    ) = MessageRepository(messageApi, userDao, authorizedUserDao, dialogDao, messageDao)
}