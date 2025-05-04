package com.example.reflect.presentation.screens.login

import com.example.reflect.domain.model.LoginModel
import com.example.reflect.domain.model.UserModel

sealed class LoginState {
    data object Idle: LoginState()
    data object Loading: LoginState()
    data class SuccessGetProfile(val userModel: UserModel): LoginState()
    data class SuccessLogin(val loginModel: LoginModel): LoginState()
    data class Error(val message: String, val code: Int): LoginState()
}