package com.example.reflect.data.repository.ai

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.ai.ResetAIContextRepository
import javax.inject.Inject

class ResetAIContextRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : ResetAIContextRepository {
    override suspend fun resetContext() {
        val response = remoteData.resetContext()
        if (response.isSuccessful) {
            Log.d("OkHttp", "Контекст обновлён, спасибо Олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}