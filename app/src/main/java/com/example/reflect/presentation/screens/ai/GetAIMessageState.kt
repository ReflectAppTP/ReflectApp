package com.example.reflect.presentation.screens.ai

import com.example.reflect.domain.model.AIGetMessageModel

sealed class GetAIMessageState {
    data object Idle: GetAIMessageState()
    data object Loading: GetAIMessageState()
    data class Success(val messageModel: AIGetMessageModel): GetAIMessageState()
    data class Error(val message: String): GetAIMessageState()
}