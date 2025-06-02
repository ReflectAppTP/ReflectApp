package com.example.reflect.presentation.screens.profile

import com.example.reflect.domain.model.GetUserByIdModel

sealed class GetUserByIdState {
    data object Idle: GetUserByIdState()
    data object Loading: GetUserByIdState()
    data class Success(val user: GetUserByIdModel): GetUserByIdState()
    data class Error(val message: String): GetUserByIdState()
}