package com.example.reflect.data.repository.ai

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.ai.AISendMessageRequestDTO
import com.example.reflect.data.dto.ai.AISendMessageResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.AISendMessageModel
import com.example.reflect.domain.repository.ai.SendAIMessageRepository
import javax.inject.Inject

class SendAIMessageRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): SendAIMessageRepository {
    override suspend fun postMessageToAI(content: String): AISendMessageModel {
        val response = remoteData.postMessageToAI(
            AISendMessageRequestDTO(content)
        )
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun AISendMessageResponseDTO.toDomain() = AISendMessageModel(
        messageId = this.messageId,
        sessionId = this.sessionId,
        status = this.status
    )
}