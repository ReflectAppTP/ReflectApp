package com.example.reflect.domain.usecase

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.EditStateRepository
import com.example.reflect.presentation.screens.addState.RecordState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class EditStateUseCase @Inject constructor(
    private val editStateRepository: EditStateRepository
) {
    suspend operator fun invoke(id: Int, value: Int,
                                description: String,
                                firstTagsIndices: List<Int>,
                                secondTagsIndices: List<Int>): Flow<RecordState> = flow {
        try {
            val recordModel = editStateRepository.editState(id, value, description, firstTagsIndices, secondTagsIndices)
            emit(RecordState.Success(recordModel))
        } catch (e: RetrofitException) {
            emit(RecordState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(RecordState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(RecordState.Error(e.message.toString()))
        }
    }
}