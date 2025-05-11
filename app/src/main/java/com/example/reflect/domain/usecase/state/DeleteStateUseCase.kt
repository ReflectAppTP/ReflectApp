package com.example.reflect.domain.usecase.state

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.state.DeleteStateRepository
import com.example.reflect.presentation.screens.addState.RecordState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class DeleteStateUseCase @Inject constructor(
    private val deleteStateRepository: DeleteStateRepository
) {
    suspend operator fun invoke(id: Int): Flow<RecordState> = flow {
        try {
            val response = deleteStateRepository.deleteState(id)
            emit(RecordState.Success(null))
        } catch (e: RetrofitException) {
            emit(RecordState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(RecordState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(RecordState.Error(e.message.toString()))
        }
    }
}