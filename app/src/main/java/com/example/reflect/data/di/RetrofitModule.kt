package com.example.reflect.data.di

import android.util.Log
import com.example.reflect.data.remote.api.RetrofitService
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.data.repository.RegistrationRepositoryImpl
import com.example.reflect.domain.repository.RegistrationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideBaseUrl() : String = "http://185.185.71.233/"

    @Provides
    @Singleton
    fun profideOkHttpClient() : OkHttpClient =
        OkHttpClient
            .Builder()
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
}