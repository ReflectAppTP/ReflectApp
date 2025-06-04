package com.example.reflect.presentation.screens.login

sealed class LoginIntent {
    data object LoginUser: LoginIntent()
    data object LoginLikeGuest: LoginIntent()
}