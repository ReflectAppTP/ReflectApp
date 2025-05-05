package com.example.reflect.data.di

import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.data.repository.AddStateRepositoryImpl
import com.example.reflect.data.repository.GetFirstTagsRepositoryImpl
import com.example.reflect.data.repository.GetProfileRepositoryImpl
import com.example.reflect.data.repository.GetSecondTagsRepositoryImpl
import com.example.reflect.data.repository.LoginRepositoryImpl
import com.example.reflect.data.repository.RefreshRepositoryImpl
import com.example.reflect.data.repository.RegistrationRepositoryImpl
import com.example.reflect.domain.repository.AddStateRepository
import com.example.reflect.domain.repository.GetFirstTagsRepository
import com.example.reflect.domain.repository.GetProfileRepository
import com.example.reflect.domain.repository.GetSecondTagsRepository
import com.example.reflect.domain.repository.LoginRepository
import com.example.reflect.domain.repository.RefreshRepository
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

    @Provides
    @Singleton
    fun provideLoginRepository(
        remoteData: RetrofitRemoteData
    ): LoginRepository = LoginRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetProfileRepository(
        remoteData: RetrofitRemoteData
    ): GetProfileRepository = GetProfileRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideRefreshRepository(
        remoteData: RetrofitRemoteData
    ): RefreshRepository = RefreshRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetFirstTagsRepository(
        remoteData: RetrofitRemoteData
    ): GetFirstTagsRepository = GetFirstTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetSecondTagsRepository(
        remoteData: RetrofitRemoteData
    ): GetSecondTagsRepository = GetSecondTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideAddStateRepository(
        remoteData: RetrofitRemoteData
    ): AddStateRepository = AddStateRepositoryImpl(remoteData)
}