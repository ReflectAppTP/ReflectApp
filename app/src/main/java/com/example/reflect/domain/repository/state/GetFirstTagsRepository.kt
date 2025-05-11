package com.example.reflect.domain.repository.state

import com.example.reflect.domain.model.TagModel

interface GetFirstTagsRepository {
    suspend fun getFirstTags(): List<TagModel>
}