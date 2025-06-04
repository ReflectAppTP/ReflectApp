package com.example.reflect.presentation.screens.premium

sealed class UpdatePremiumState {
    data object Idle: UpdatePremiumState()
    data object Loading: UpdatePremiumState()
    data object Success: UpdatePremiumState()
    data class Error(val message: String): UpdatePremiumState()
}