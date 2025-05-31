package com.example.reflect.data.dto.ai

import com.google.gson.annotations.SerializedName

data class AISendMessageRequestDTO(
    @SerializedName("content") val content: String
)
