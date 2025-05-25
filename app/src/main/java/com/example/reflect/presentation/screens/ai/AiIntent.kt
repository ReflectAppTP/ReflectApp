package com.example.reflect.presentation.screens.ai

sealed class AiIntent {
    data object SendAiMessage: AiIntent()
}