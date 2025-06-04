package com.example.reflect.domain.usecase.statistic

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.statistic.GetStatisticTagsRepository
import com.example.reflect.presentation.screens.statistics.states.StatisticTagState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetStatisticTagsUseCase @Inject constructor(
    private val getStatisticTagsRepository: GetStatisticTagsRepository
) {
    suspend operator fun invoke(startDate: String, endDate: String): Flow<StatisticTagState> = flow {
        try {
            emit(StatisticTagState.Loading)
            val records = getStatisticTagsRepository.getStatisticTags(startDate, endDate)
            emit(StatisticTagState.Success(records))
        }catch (e: RetrofitException) {
            emit(StatisticTagState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(StatisticTagState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(StatisticTagState.Error(e.message.toString()))
        }
    }
}