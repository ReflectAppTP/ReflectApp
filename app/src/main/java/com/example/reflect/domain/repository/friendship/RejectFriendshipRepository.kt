package com.example.reflect.domain.repository.friendship

interface RejectFriendshipRepository {
    suspend fun rejectFriendship(id: Int)
}