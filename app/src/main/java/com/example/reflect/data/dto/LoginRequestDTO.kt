package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)