package com.example.reflect.presentation.screens.records

sealed class GetStreakState {
    data object Idle: GetStreakState()
    data object Loading: GetStreakState()
    data class Success(val streak: Int): GetStreakState()
    data class Error(val message: String): GetStreakState()
}