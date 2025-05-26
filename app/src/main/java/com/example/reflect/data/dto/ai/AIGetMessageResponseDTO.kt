package com.example.reflect.data.dto.ai

import com.google.gson.annotations.SerializedName

data class AIGetMessageResponseDTO(
    @SerializedName("status") val status: String,
    @SerializedName("response") val response: String?,
    @SerializedName("created_at") val createdAt: String
)