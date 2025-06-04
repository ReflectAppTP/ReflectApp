package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class GetFriendsResponseDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("is_premium") val isPremium: Boolean
)