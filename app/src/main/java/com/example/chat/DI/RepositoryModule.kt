package com.example.chat.DI

import com.example.chat.DB.dao.AuthorizedUserDao
import com.example.chat.DB.dao.UserDao
import com.example.chat.model.AuthorizationRepository
import com.example.chat.network.api.user.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideAuthorizationRepository(
        userApi: UserApi,
        userDao: UserDao,
        authorizedUserDao: AuthorizedUserDao
    ) = AuthorizationRepository(userApi, userDao, authorizedUserDao)

}