package com.example.reflect.domain.repository.state

import com.example.reflect.domain.model.RecordModel

interface EditStateRepository {
    suspend fun editState(id: Int, value: Int, description: String, firstTagsIndices: List<Int>, secondTagsIndices: List<Int>): RecordModel
}