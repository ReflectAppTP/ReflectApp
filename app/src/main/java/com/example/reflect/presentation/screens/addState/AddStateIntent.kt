package com.example.reflect.presentation.screens.addState

sealed class AddStateIntent {
    data object AddState: AddStateIntent()
    data object EditState: AddStateIntent()
}