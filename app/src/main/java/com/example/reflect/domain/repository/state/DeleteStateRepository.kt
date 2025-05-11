package com.example.reflect.domain.repository.state

interface DeleteStateRepository {
    suspend fun deleteState(id: Int)
}