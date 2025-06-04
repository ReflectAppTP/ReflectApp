package com.example.reflect.domain.repository.friendship

interface ChangeVisibilityRepository {
    suspend fun updateVisibility(visibility: String)
}