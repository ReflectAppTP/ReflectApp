package com.example.reflect.domain.repository.friendship

interface SendFriendshipRequestRepository {
    suspend fun sendFriendshipRequest(toUserId: Int)
}