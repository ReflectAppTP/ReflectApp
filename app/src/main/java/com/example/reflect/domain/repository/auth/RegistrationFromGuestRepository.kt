package com.example.reflect.domain.repository.auth

import com.example.reflect.domain.model.UserModel

interface RegistrationFromGuestRepository {
    suspend fun registerFromGuest(username: String, email: String, password: String): UserModel
}