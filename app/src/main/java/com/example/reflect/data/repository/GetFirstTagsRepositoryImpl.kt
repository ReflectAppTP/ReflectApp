package com.example.reflect.data.repository

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.TagDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.TagModel
import com.example.reflect.domain.repository.GetFirstTagsRepository
import javax.inject.Inject

class GetFirstTagsRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : GetFirstTagsRepository {
    override suspend fun getFirstTags(): List<TagModel> {
        val response = remoteData.getFirstTags()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun TagDTO.toDomain() = TagModel(
        id = this.id,
        name = this.name,
        emoji = this.emoji
    )

    private fun List<TagDTO>.toDomain() = this.map { it.toDomain() }
}