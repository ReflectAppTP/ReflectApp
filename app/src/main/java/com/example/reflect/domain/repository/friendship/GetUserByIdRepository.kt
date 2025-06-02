package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.GetUserByIdModel

interface GetUserByIdRepository {
    suspend fun getUserById(id: Int): GetUserByIdModel
}