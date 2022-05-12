package com.example.chat.DI

import android.content.Context
import com.example.chat.ImageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun provideImageHelper(@ApplicationContext applicationContext: Context): ImageHelper =
        ImageHelper(applicationContext)
}
