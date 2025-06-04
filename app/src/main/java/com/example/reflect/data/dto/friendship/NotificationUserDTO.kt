package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class NotificationUserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String
)