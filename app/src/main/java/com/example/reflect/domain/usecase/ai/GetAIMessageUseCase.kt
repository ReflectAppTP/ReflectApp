package com.example.reflect.domain.usecase.ai

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.ai.GetAIMessageRepository
import com.example.reflect.presentation.screens.ai.GetAIMessageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetAIMessageUseCase @Inject constructor(
    private val getAIMessageRepository: GetAIMessageRepository
) {
    suspend operator fun invoke(
        messageId: Int
    ): Flow<GetAIMessageState> = flow {
        emit(GetAIMessageState.Loading)
        try {
            val messageModel = getAIMessageRepository.getAIMessage(messageId)
            emit(GetAIMessageState.Success(messageModel))
        } catch (e: RetrofitException) {
            emit(GetAIMessageState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetAIMessageState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(GetAIMessageState.Error(e.message.toString()))
        }
    }
}