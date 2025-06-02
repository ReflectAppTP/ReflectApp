package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.NotificationUserModel
import kotlinx.coroutines.flow.Flow

interface WebSocketFriendshipRepository {
    fun notifications(): Flow<NotificationUserModel>
    suspend fun close()
}