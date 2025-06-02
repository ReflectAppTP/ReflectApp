package com.example.reflect.domain.usecase.friendship

import com.example.reflect.domain.model.NotificationFriendshipModel
import com.example.reflect.domain.repository.friendship.WebSocketFriendshipRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketFriendshipUseCase @Inject constructor(
    private val webSocketFriendshipRepository: WebSocketFriendshipRepository
) {
    operator fun invoke(): Flow<NotificationFriendshipModel> = webSocketFriendshipRepository.notifications()

    suspend fun disconnect() = webSocketFriendshipRepository.close()
}