package com.example.reflect.data.dto.login

import com.google.gson.annotations.SerializedName

data class RefreshRequestDTO(
    @SerializedName("refresh") val refreshToken: String
)