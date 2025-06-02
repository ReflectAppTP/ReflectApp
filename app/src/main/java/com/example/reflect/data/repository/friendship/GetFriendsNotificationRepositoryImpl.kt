package com.example.reflect.data.repository.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.friendship.NotificationFriendshipDTO
import com.example.reflect.data.dto.friendship.NotificationUserDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.NotificationFriendshipModel
import com.example.reflect.domain.model.NotificationUserModel
import com.example.reflect.domain.repository.friendship.GetFriendsNotificationRepository
import javax.inject.Inject

class GetFriendsNotificationRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetFriendsNotificationRepository {
    override suspend fun getFriendshipNotification(): List<NotificationFriendshipModel> {
        val response = remoteData.getFriendshipNotification()
        if (response.isSuccessful) {
            Log.d("Raw response", " ${response.raw()}")
            Log.d("Response body", " ${response.body()}")
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun NotificationUserDTO.toDomain() = NotificationUserModel(
        id = this.id,
        username = this.username
    )

    private fun NotificationFriendshipDTO.toDomain() = NotificationFriendshipModel(
        type = this.type,
        message = this.message,
        fromUser = this.fromUser.toDomain()
    )

    private fun List<NotificationFriendshipDTO>.toDomain() = this.map { it.toDomain() }
}