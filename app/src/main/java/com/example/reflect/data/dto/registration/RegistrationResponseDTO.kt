package com.example.reflect.data.dto.registration

import com.example.reflect.data.dto.UserDTO
import com.google.gson.annotations.SerializedName

data class RegistrationResponseDTO(
    @SerializedName("user") val user: UserDTO,
    @SerializedName("message") val message: String
)