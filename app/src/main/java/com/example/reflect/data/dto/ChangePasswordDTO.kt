package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class ChangePasswordDTO(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)
