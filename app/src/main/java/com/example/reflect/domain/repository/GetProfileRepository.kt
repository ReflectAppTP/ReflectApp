package com.example.reflect.domain.repository

import com.example.reflect.domain.model.UserModel

interface GetProfileRepository {
    suspend fun getUser(): UserModel
}