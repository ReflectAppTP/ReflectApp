package com.example.reflect.domain.repository.auth

import com.example.reflect.domain.model.LoginModel

interface RefreshRepository {
    suspend fun getAccessToken(refreshToken: String): LoginModel
}