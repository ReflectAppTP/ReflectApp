package com.example.reflect.domain.model

data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val createdAt: String,
    val isAdmin: Boolean,
    val isPremium: Boolean,
    )