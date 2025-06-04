package com.example.reflect.domain.usecase.state

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.state.GetStreakRepository
import com.example.reflect.presentation.screens.records.GetStreakState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetStreakUseCase @Inject constructor(
    private val getStreakRepository: GetStreakRepository
) {
    suspend operator fun invoke(): Flow<GetStreakState> = flow {
        emit(GetStreakState.Loading)
        try {
            val streak = getStreakRepository.getStreak()
            emit(GetStreakState.Success(streak))
        } catch (e: RetrofitException) {
            emit(GetStreakState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetStreakState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(GetStreakState.Error(e.message.toString()))
        }
    }
}