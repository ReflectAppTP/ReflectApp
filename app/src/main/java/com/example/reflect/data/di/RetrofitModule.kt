package com.example.reflect.data.di

import android.content.Context
import android.util.Log
import com.example.reflect.common.interceptor.AccessTokenInterceptor
import com.example.reflect.data.remote.api.RetrofitService
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.data.repository.friendship.WebSocketFriendshipRepositoryImpl
import com.example.reflect.domain.repository.friendship.WebSocketFriendshipRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideBaseUrl() : String = "https://reflect-app.ru/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AccessTokenInterceptor,
    ) : OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                },
            ).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        BASE_URL: String,
        okHttpClient: OkHttpClient
    ) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit) : RetrofitService = retrofit.create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitRemoteData(retrofitService: RetrofitService) : RetrofitRemoteData = RetrofitRemoteData(retrofitService)

    @Provides
    @Singleton
    fun provideWebSocketFriendship(
        okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): WebSocketFriendshipRepository = WebSocketFriendshipRepositoryImpl(okHttpClient, context)
}