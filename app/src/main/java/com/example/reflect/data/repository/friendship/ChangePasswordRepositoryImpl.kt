package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ChangePasswordDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.ChangePasswordRepository
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): ChangePasswordRepository {
    override suspend fun updatePassword(oldPassword: String, newPassword: String) {
        val response = remoteData.updatePassword(
            ChangePasswordDTO(oldPassword, newPassword)
        )
        if (response.isSuccessful) {
            Log.d("OkHttp", "Пароль изменён успешно, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}