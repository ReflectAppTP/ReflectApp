package com.example.reflect.domain.usecase.ai

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.ai.SendAIMessageRepository
import com.example.reflect.presentation.screens.ai.SendAIMessageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class SendAIMessageUseCase @Inject constructor(
    private val sendAIMessageRepository: SendAIMessageRepository
) {
    suspend operator fun invoke(
        content: String
    ): Flow<SendAIMessageState> = flow {
        emit(SendAIMessageState.Loading)
        try {
            val aiSendMessageModel = sendAIMessageRepository.postMessageToAI(content)
            emit(SendAIMessageState.Success(aiSendMessageModel))
        } catch (e: RetrofitException) {
            emit(SendAIMessageState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(SendAIMessageState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(SendAIMessageState.Error(e.message.toString()))
        }
    }
}