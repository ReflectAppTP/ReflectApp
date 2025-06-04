package com.example.reflect.presentation.screens.friends

sealed class DoWithNotificationState {
    data object Idle: DoWithNotificationState()
    data object Loading: DoWithNotificationState()
    data object Success: DoWithNotificationState()
    data class Error(val message: String): DoWithNotificationState()
}