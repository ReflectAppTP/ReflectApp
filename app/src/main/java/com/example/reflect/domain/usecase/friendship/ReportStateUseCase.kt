package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ReportStateRepository
import com.example.reflect.presentation.screens.friends.ReportState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ReportStateUseCase @Inject constructor(
    private val reportStateRepository: ReportStateRepository
) {
    suspend operator fun invoke(id: Int, report: String): Flow<ReportState> = flow {
        emit(ReportState.Loading)
        try {
            val response = reportStateRepository.reportState(id, report)
            emit(ReportState.SuccessState)
        } catch (e: RetrofitException) {
            emit(ReportState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(ReportState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(ReportState.Error(e.message.toString()))
        }
    }
}