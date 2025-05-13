package com.example.reflect.domain.usecase.statistic

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.domain.repository.statistic.GetYearlyAverageRepository
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetYearlyAverageUseCase @Inject constructor(
    private val getYearlyAverageRepository: GetYearlyAverageRepository
) {
    suspend operator fun invoke(): Flow<LineChartState> = flow {
        try {
            emit(LineChartState.Loading)
            val records = getYearlyAverageRepository.getYearlyAverage()
            emit(LineChartState.Success(records.toEntries(), TimeRange.YEAR))
        } catch (e: RetrofitException) {
            emit(LineChartState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(LineChartState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(LineChartState.Error(e.message.toString()))
        }
    }

    private fun List<StatisticAverageModel>.toEntries(): List<Entry> {
        val sortedList = this.sortedBy { it.date }

        return sortedList.mapIndexed { index, model ->
            Entry(index.toFloat(), model.averageMood, model.date)
        }
    }
}