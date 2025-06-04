package com.example.reflect.presentation.screens.passwordReset

sealed class ResetPasswordIntent {
    data object ResetPassword: ResetPasswordIntent()
}