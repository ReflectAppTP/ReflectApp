package com.example.reflect.domain.repository.state

import com.example.reflect.domain.model.RecordModel

interface AddStateRepository {
    suspend fun addState(value: Int, description: String, firstTagsIndices: List<Int>, secondTagsIndices: List<Int>): RecordModel
}