package com.example.reflect.presentation.screens.profile

sealed class SettingsProfileIntent {
    data object SaveChanges: SettingsProfileIntent()
}