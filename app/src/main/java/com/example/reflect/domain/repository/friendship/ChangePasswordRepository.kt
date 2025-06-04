package com.example.reflect.domain.repository.friendship

interface ChangePasswordRepository {
    suspend fun updatePassword(oldPassword: String, newPassword: String)
}