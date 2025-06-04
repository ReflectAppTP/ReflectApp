package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class UserIdDTO(
    @SerializedName("to_user_id") val toUserId: Int
)