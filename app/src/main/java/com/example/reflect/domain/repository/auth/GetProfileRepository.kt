package com.example.reflect.domain.repository.auth

import com.example.reflect.domain.model.UserModel

interface GetProfileRepository {
    suspend fun getUser(): UserModel
}