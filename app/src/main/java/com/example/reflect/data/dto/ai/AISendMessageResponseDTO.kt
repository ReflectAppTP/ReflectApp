package com.example.reflect.data.dto.ai

import com.google.gson.annotations.SerializedName

data class AISendMessageResponseDTO(
    @SerializedName("message_id") val messageId: Int,
    @SerializedName("session_id") val sessionId: Int,
    @SerializedName("status") val status: String
)
