package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("refresh") val refresh: String,
    @SerializedName("access") val access: String
)