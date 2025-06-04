package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.AcceptFriendshipRepository
import javax.inject.Inject

class AcceptFriendshipRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): AcceptFriendshipRepository {
    override suspend fun acceptFriendship(id: Int) {
        val response = remoteData.acceptFriendship(id)
        if (response.isSuccessful) {
            Log.d("OkHttp", "Запрос в друзья принят, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}