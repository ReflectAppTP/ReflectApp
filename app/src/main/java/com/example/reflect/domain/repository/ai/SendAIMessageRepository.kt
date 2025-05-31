package com.example.reflect.domain.repository.ai

import com.example.reflect.domain.model.AISendMessageModel

interface SendAIMessageRepository {
    suspend fun postMessageToAI(content: String): AISendMessageModel
}