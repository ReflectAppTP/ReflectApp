package com.example.reflect.domain.repository

interface DeleteStateRepository {
    suspend fun deleteState(id: Int)
}