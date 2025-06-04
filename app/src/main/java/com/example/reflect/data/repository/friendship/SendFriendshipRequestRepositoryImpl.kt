package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.UserIdDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.friendship.SendFriendshipRequestRepository
import javax.inject.Inject

class SendFriendshipRequestRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : SendFriendshipRequestRepository {
    override suspend fun sendFriendshipRequest(toUserId: Int) {
        val response = remoteData.sendFriendshipRequest(UserIdDTO(toUserId))
        if (response.isSuccessful) {
            Log.d("OkHttp", "Запрос в друзья отправлен, спасибо Олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}