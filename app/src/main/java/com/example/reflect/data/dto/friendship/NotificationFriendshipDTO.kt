package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class NotificationFriendshipDTO(
    @SerializedName("type") val type: String,
    @SerializedName("message") val message: String,
    @SerializedName("from_user") val fromUser: NotificationUserDTO
)