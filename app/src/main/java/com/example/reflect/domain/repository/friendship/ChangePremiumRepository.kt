package com.example.reflect.domain.repository.friendship

interface ChangePremiumRepository {
    suspend fun updatePremium(isPremium: Boolean)
}