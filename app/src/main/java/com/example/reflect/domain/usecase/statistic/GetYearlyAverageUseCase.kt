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
            val records = getYearlyAverageRepository.getYearlyAverage()
            emit(LineChartState.Success(records.map { it.toEntry() }, TimeRange.YEAR))
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
        return Entry((calendar.get(Calendar.DAY_OF_MONTH) - 1).toFloat(),this.averageMood)
    }
}