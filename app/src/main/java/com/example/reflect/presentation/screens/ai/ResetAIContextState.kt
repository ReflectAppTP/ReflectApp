package com.example.reflect.presentation.screens.ai

sealed class ResetAIContextState {
    data object Idle: ResetAIContextState()
    data object Loading: ResetAIContextState()
    data object Success: ResetAIContextState()
    data class Error(val message: String): ResetAIContextState()
}