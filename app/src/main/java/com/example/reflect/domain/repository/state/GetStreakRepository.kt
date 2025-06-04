package com.example.reflect.domain.repository.state

interface GetStreakRepository {
    suspend fun getStreak(): Int
}