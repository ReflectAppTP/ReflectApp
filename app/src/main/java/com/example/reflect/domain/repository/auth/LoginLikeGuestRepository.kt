package com.example.reflect.domain.repository.auth

import com.example.reflect.domain.model.UserModel

interface LoginLikeGuestRepository {
    suspend fun loginLikeGuest(): UserModel
}