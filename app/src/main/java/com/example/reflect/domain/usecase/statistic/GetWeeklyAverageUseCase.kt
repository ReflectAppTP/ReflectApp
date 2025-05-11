package com.example.reflect.domain.usecase.statistic

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.domain.repository.statistic.GetWeeklyAverageRepository
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetWeeklyAverageUseCase @Inject constructor(
    private val getWeeklyAverageRepository: GetWeeklyAverageRepository
) {
    suspend operator fun invoke(): Flow<LineChartState> = flow {
        try {
            val records = getWeeklyAverageRepository.getWeeklyAverage()
            emit(LineChartState.Success(records.map { it.toEntry() }, TimeRange.WEEK))
        } catch (e: RetrofitException) {
            emit(LineChartState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(LineChartState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(LineChartState.Error(e.message.toString()))
        }
    }

    private fun StatisticAverageModel.toEntry(): Entry {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this.date)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        Log.d("OK use", calendar.get(Calendar.DAY_OF_WEEK).toString())
        return Entry((calendar.get(Calendar.DAY_OF_WEEK) - 1).toFloat(),this.averageMood)
    }
}