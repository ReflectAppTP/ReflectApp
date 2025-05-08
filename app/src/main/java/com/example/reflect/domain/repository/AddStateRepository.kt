package com.example.reflect.domain.repository

import com.example.reflect.domain.model.RecordModel

interface AddStateRepository {
    suspend fun addState(value: Int, description: String, firstTagsIndices: List<Int>, secondTagsIndices: List<Int>): RecordModel
}