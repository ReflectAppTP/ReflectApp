package com.example.reflect.presentation.screens.premium

sealed class UpdatePremiumIntent {
    data class Update(val isPremium: Boolean): UpdatePremiumIntent()
}