package com.example.reflect.presentation.screens.login

import com.example.reflect.domain.model.LoginModel

sealed class LoginState {
    data object Idle: LoginState()
    data object Loading: LoginState()
    data class Success(val loginModel: LoginModel): LoginState()
    data class Error(val message: String): LoginState()
}