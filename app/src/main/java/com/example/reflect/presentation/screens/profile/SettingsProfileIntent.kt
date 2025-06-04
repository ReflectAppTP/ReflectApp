package com.example.reflect.presentation.screens.profile

sealed class SettingsProfileIntent {
    data class Update(
        val username: String?,
        val oldPassword: String?,
        val newPassword: String?,
        val visibility: String
    ): SettingsProfileIntent()
}