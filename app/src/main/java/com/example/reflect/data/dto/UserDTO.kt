package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_admin") val isAdmin: Boolean,
    @SerializedName("is_premium") val isPremium: Boolean,

)