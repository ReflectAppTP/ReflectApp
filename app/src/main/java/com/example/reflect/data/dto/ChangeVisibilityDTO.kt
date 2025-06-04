package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class ChangeVisibilityDTO(
    @SerializedName("visibility") val visibility: String
)