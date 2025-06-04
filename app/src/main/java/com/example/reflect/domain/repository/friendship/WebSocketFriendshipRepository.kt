package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.NotificationFriendshipModel
import kotlinx.coroutines.flow.Flow

interface WebSocketFriendshipRepository {
    fun notifications(): Flow<NotificationFriendshipModel>
    suspend fun close()
}