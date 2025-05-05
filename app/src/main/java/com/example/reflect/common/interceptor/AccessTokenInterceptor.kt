package com.example.reflect.common.interceptor

import android.content.Context
import com.example.reflect.common.prefs.AccountPrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = AccountPrefs.getAuthToken(context)

        val request = originalRequest.addAuthHeader(accessToken)
        return chain.proceed(request)
    }

    private fun Request.addAuthHeader(token: String?) = newBuilder()
        .apply {
            token?.let {
                header("Authorization", "Bearer $it")
            }
        }
        .build()
}