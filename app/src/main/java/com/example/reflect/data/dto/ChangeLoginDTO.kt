package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class ChangeLoginDTO(
    @SerializedName("username") val login: String
)
