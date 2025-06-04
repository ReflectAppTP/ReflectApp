package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.DeleteUserRepository
import javax.inject.Inject

class DeleteUserRepositoryImpl @Inject constructor(
    private val retrofitRemoteData: RetrofitRemoteData
): DeleteUserRepository {
    override suspend fun deleteUser() {
        val response = retrofitRemoteData.deleteUser()
        if (response.isSuccessful) {
            Log.d("Okhttp", "Пользователь удалён, спасибо Олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}