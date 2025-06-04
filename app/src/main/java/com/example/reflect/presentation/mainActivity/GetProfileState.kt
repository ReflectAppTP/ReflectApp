package com.example.reflect.presentation.mainActivity

import com.example.reflect.domain.model.LoginModel

sealed class GetProfileState {
    data object Idle: GetProfileState()
    data object Loading: GetProfileState()
    data class Success(val loginModel: LoginModel): GetProfileState()
    data class Error(val message: String, val code: Int): GetProfileState()
}