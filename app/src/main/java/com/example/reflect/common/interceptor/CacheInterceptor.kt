package com.example.reflect.common.interceptor

import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(2, TimeUnit.MINUTES)
            .build()
        Log.d("OkHttp cache", "Запрос кеширован")
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}