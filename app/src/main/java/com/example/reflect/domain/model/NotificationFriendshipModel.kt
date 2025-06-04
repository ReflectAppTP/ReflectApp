package com.example.reflect.domain.model

data class NotificationFriendshipModel(
    val type: String,
    val message: String,
    val fromUser: NotificationUserModel
)