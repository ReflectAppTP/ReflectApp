package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ChangeVisibilityDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.ChangeVisibilityRepository
import javax.inject.Inject

class ChangeVisibilityRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : ChangeVisibilityRepository {
    override suspend fun updateVisibility(visibility: String) {
        val response = remoteData.updateVisibility(
            ChangeVisibilityDTO(visibility)
        )
        if (response.isSuccessful) {
            Log.d("OkHttp", "Видимость профиля изменена успешно, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }

    }
}