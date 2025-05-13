package com.example.reflect.domain.usecase.state

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.state.GetStatesRepository
import com.example.reflect.presentation.screens.records.GetRecordsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    private val getStatesRepository: GetStatesRepository
) {
    suspend operator fun invoke(date: String): Flow<GetRecordsState> = flow {
        emit(GetRecordsState.Loading)
        try {
            val records = getStatesRepository.getStates(date)
            if (records.isEmpty())
                emit(GetRecordsState.EmptyContent)
            else
                emit(GetRecordsState.Success(records))
        } catch (e: RetrofitException) {
            emit(GetRecordsState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetRecordsState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(GetRecordsState.Error(e.message.toString()))
        }
    }
}