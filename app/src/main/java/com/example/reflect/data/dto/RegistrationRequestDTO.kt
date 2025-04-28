package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class RegistrationRequestDTO(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)