package com.example.reflect.domain.repository.friendship

interface ChangeLoginRepository {
    suspend fun updateLogin(username: String)
}