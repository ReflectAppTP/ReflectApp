package com.example.reflect.presentation.screens.ai

import com.example.reflect.domain.model.AISendMessageModel

sealed class SendAIMessageState {
    data object Idle: SendAIMessageState()
    data object Loading: SendAIMessageState()
    data class Success(val message: AISendMessageModel): SendAIMessageState()
    data class Error(val message: String): SendAIMessageState()
}