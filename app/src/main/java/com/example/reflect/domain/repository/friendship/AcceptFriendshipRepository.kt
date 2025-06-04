package com.example.reflect.domain.repository.friendship

interface AcceptFriendshipRepository {
    suspend fun acceptFriendship(id: Int)
}