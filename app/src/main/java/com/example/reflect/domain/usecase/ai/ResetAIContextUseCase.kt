package com.example.reflect.domain.usecase.ai

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.ai.ResetAIContextRepository
import com.example.reflect.presentation.screens.ai.ResetAIContextState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ResetAIContextUseCase @Inject constructor(
    private val resetAIContextRepository: ResetAIContextRepository
) {
    suspend operator fun invoke(): Flow<ResetAIContextState> = flow {
        emit(ResetAIContextState.Loading)
        try {
            val response = resetAIContextRepository.resetContext()
            emit(ResetAIContextState.Success)
        } catch (e: RetrofitException) {
            emit(ResetAIContextState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(ResetAIContextState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(ResetAIContextState.Error(e.message.toString()))
        }
    }
}