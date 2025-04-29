package com.example.reflect.data.di

import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.data.repository.RegistrationRepositoryImpl
import com.example.reflect.domain.repository.RegistrationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        remoteData: RetrofitRemoteData
    ): RegistrationRepository = RegistrationRepositoryImpl(remoteData)
}