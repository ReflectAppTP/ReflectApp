package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ChangePremiumDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.ChangePremiumRepository
import javax.inject.Inject

class ChangePremiumRepositoryImpl @Inject constructor(
    private val remoteDate: RetrofitRemoteData
) : ChangePremiumRepository {
    override suspend fun updatePremium(isPremium: Boolean) {
        val response = remoteDate.updatePremium(
            ChangePremiumDTO(isPremium)
        )
        if (response.isSuccessful) {
            Log.d("OkHttp", "Премиум успешно изменён, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}