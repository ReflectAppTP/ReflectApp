package com.example.reflect.domain.repository

import com.example.reflect.domain.model.LoginModel

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginModel
}