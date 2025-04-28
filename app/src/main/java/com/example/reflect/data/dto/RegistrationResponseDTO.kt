package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class RegistrationResponseDTO(
    @SerializedName("user") val user: UserDTO,
    @SerializedName("message") val message: String
)