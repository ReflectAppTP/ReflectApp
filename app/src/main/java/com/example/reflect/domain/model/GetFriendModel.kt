package com.example.reflect.domain.model

data class GetFriendModel(
    val id: Int,
    val login: String,
    val email: String,
    val isPremium: Boolean
)