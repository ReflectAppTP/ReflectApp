package com.example.reflect.presentation.screens.profile

sealed class DeleteUserState {
    data object Idle: DeleteUserState()
    data object Loading: DeleteUserState()
    data object Success: DeleteUserState()
    data class Error(val message: String): DeleteUserState()
}