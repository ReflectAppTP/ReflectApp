package com.example.reflect.presentation.screens.profile

sealed class UpdateProfileState {
    data object Idle: UpdateProfileState()
    data object Loading: UpdateProfileState()
    data object Success: UpdateProfileState()
    data class Error(val message: String): UpdateProfileState()
}