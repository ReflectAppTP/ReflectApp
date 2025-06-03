package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ReportUserRepository
import com.example.reflect.presentation.screens.friends.ReportState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ReportUserUseCase @Inject constructor(
    private val reportUserRepository: ReportUserRepository
) {
    suspend operator fun invoke(id: Int, report: String): Flow<ReportState> = flow {
        emit(ReportState.Loading)
        try {
            val response = reportUserRepository.reposrtUser(id, report)
            emit(ReportState.SuccessUser)
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