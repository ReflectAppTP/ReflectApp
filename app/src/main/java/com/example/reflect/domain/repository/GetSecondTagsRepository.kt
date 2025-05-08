package com.example.reflect.domain.repository

import com.example.reflect.domain.model.TagModel

interface GetSecondTagsRepository {
    suspend fun getSecondTags(): List<TagModel>
}