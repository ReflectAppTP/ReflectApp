package com.example.reflect.domain.model

data class GetShortUserModel(
    val id: Int,
    val login: String,
    val email: String,
    val isPremium: Boolean
)