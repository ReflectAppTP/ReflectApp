package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.RejectFriendshipRepository
import javax.inject.Inject

class RejectFriendshipRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): RejectFriendshipRepository {
    override suspend fun rejectFriendship(id: Int) {
        val response = remoteData.rejectFriendship(id)
        if (response.isSuccessful) {
            Log.d("OkHttp", "Запрос в друзья отклонён, спасибо олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}