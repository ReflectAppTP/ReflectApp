package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.GetShortUserModel

interface SearchUsersRepository {
    suspend fun searchUsers(username: String): List<GetShortUserModel>
}