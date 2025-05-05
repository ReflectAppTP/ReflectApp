package com.example.reflect.domain.usecase

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.model.TagModel
import com.example.reflect.domain.repository.AddStateRepository
import com.example.reflect.presentation.screens.addState.RecordState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class AddStateUseCase @Inject constructor(
    private val addStateRepository: AddStateRepository
) {
    suspend operator fun invoke(
        value: Int,
        description: String,
        firstTagsIndices: List<Int>,
        secondTagsIndices: List<Int>
    ): Flow<RecordState> = flow {
        try {
            val recordModel = addStateRepository.addState(value, description, firstTagsIndices, secondTagsIndices)
            Log.d("OkHttp adds", recordModel.toString())
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