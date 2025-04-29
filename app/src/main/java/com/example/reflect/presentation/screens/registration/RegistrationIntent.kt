package com.example.reflect.presentation.screens.registration

sealed class RegistrationIntent {
    data object RegistrateUser: RegistrationIntent()
}