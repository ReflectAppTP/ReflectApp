package com.example.reflect.domain.usecase.statistic

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.model.StatisticMoodModel
import com.example.reflect.domain.repository.statistic.GetStateFrequencyRepository
import com.example.reflect.presentation.screens.statistics.states.PieChartState
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetFrequencyUseCase @Inject constructor(
    private val getStateFrequencyRepository: GetStateFrequencyRepository
) {
    suspend operator fun invoke(startDate: String, endDate: String): Flow<PieChartState> = flow {
        try {
            val records = getStateFrequencyRepository.getStateFrequency(startDate, endDate)
            emit(PieChartState.Success(records.map { it.toPieEntry() }))
        } catch (e: RetrofitException) {
            emit(PieChartState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(PieChartState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(PieChartState.Error(e.message.toString()))
        }
    }

    private fun StatisticMoodModel.toPieEntry() = PieEntry(
        this.freq.toFloat(),
        getPieEntryLabel(this.state),
        this.state
    )

    private fun getPieEntryLabel(state: Int): String {
        return when (state) {
            1 -> "Ужасное"
            2 -> "Плохое"
            3 -> "Нормальное"
            4 -> "Хорошее"
            5 -> "Отличное"
            else -> throw Exception("Как среди значений от 1 до 5 появлось что то другое")
        }
    }
}