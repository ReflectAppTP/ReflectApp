package com.example.reflect.domain.repository

import com.example.reflect.domain.model.UserModel
import javax.inject.Singleton

@Singleton
interface RegistrationRepository {
    suspend fun register(username: String, email: String, password: String) : UserModel
}