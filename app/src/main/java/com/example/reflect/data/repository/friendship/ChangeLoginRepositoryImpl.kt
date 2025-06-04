package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ChangeLoginDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.ChangeLoginRepository
import javax.inject.Inject

class ChangeLoginRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): ChangeLoginRepository {
    override suspend fun updateLogin(username: String) {
        val response = remoteData.updateUsername(
            ChangeLoginDTO(username)
        )
        if (response.isSuccessful) {
            Log.d("OkHttp", "Логин изменён успешно, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}