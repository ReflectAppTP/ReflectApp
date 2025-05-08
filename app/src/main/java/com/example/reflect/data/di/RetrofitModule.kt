package com.example.reflect.data.di

import android.content.Context
import android.util.Log
import com.example.reflect.common.interceptor.AccessTokenInterceptor
import com.example.reflect.common.interceptor.CacheInterceptor
import com.example.reflect.common.interceptor.ForceCacheInterceptor
import com.example.reflect.data.remote.api.RetrofitService
import com.example.reflect.data.remote.data.RetrofitRemoteData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideBaseUrl() : String = "http://185.185.71.233/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AccessTokenInterceptor,
        cacheInterceptor: CacheInterceptor,
        forceCacheInterceptor: ForceCacheInterceptor,
        @ApplicationContext context: Context
    ) : OkHttpClient =
        OkHttpClient
            .Builder()
//            .cache(Cache(File(context.cacheDir, "http-cache"), 2L * 1024L * 1024L)) // 2 MB
//            .addNetworkInterceptor(cacheInterceptor)
//            .addInterceptor(forceCacheInterceptor)
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
}