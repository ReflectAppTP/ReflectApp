package com.example.reflect.domain.repository

import com.example.reflect.domain.model.RecordModel

interface GetStatesRepository {
    suspend fun getStates(date: String): List<RecordModel>
}