package com.example.reflect.data.di

import android.content.Context
import com.example.reflect.common.interceptor.AccessTokenInterceptor
import com.example.reflect.common.interceptor.CacheInterceptor
import com.example.reflect.common.interceptor.ForceCacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @Singleton
    fun provideAccessTokenInterceptor(
        @ApplicationContext context: Context
    ): AccessTokenInterceptor = AccessTokenInterceptor(context)

    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

    @Provides
    @Singleton
    fun provideForceCacheInterceptor(
        @ApplicationContext context: Context
    ): ForceCacheInterceptor = ForceCacheInterceptor(context)
}