package com.example.reflect.domain.repository.ai

import com.example.reflect.domain.model.AIGetMessageModel

interface GetAIMessageRepository {
    suspend fun getAIMessage(messageId: Int): AIGetMessageModel
}