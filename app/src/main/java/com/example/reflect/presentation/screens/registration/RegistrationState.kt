package com.example.reflect.presentation.screens.registration

import com.example.reflect.domain.model.UserModel

sealed class RegistrationState {
    data object Idle : RegistrationState()
    data object Loading: RegistrationState()
    data class Success(val user: UserModel) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}