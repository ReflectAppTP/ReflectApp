package com.example.reflect.data.repository.ai

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ai.AIGetMessageResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.AIGetMessageModel
import com.example.reflect.domain.repository.ai.GetAIMessageRepository
import javax.inject.Inject

class GetAIMessageRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetAIMessageRepository {
    override suspend fun getAIMessage(messageId: Int): AIGetMessageModel {
        val response = remoteData.getAIMessage(messageId)
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun AIGetMessageResponseDTO.toDomain() = AIGetMessageModel(
        status, response, createdAt
    )
}