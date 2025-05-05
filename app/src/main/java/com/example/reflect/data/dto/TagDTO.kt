package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class TagDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("emoji") val emoji: String?
)