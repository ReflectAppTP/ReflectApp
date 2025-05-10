package com.example.reflect.presentation.screens.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.example.reflect.presentation.screens.statistics.states.PieChartState
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VIewModelStatistic @Inject constructor(

): ViewModel() {

    val userIntent = Channel<StatisticIntent>(Channel.UNLIMITED)

    private var _lineChartState = MutableStateFlow<LineChartState>(LineChartState.Idle)
    val lineChartState: StateFlow<LineChartState> = _lineChartState

    private var _pieChartState = MutableStateFlow<PieChartState>(PieChartState.Idle)
    val pieChartState: StateFlow<PieChartState> = _pieChartState

    init {
        getWeekStatistic()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is StatisticIntent.WeekStatistic -> getWeekStatistic()
                    is StatisticIntent.MonthStatistic -> getMonthStatistic()
                    is StatisticIntent.YearStatistic -> getYearStatistic()
                }
            }
        }
    }

    private fun getWeekStatistic() {
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(listOf(
            Entry(0f, 9f),
            Entry(1f, 7f),
            Entry(2f, 10f),
            Entry(3f, 6f),
            Entry(4f, 8f),
            Entry(5f, 4f),
            Entry(6f, 2f)
        ))

        _pieChartState.value = PieChartState.Loading
        _pieChartState.value = PieChartState.Success(listOf(
            PieEntry(10f, "Ужасное", 1),
            PieEntry(20f, "Плохое", 2),
            PieEntry(25f, "Нормальное", 3),
            PieEntry(35f, "Хорошее", 4),
            PieEntry(10f, "Замечательное", 5)
        ))
    }

    private fun getMonthStatistic() {
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(listOf(
            Entry(0f, 1f),
            Entry(2f, 10f),
            Entry(5f, 4f),
            Entry(6f, 2f),
            Entry(7f, 1f),
            Entry(8f, 7f),
            Entry(9f, 8f),
            Entry(12f, 10f),
            Entry(15f, 3f),
            Entry(16f, 10f),
            Entry(17f, 10f),
            Entry(18f, 7f),
            Entry(20f, 1f),
            Entry(21f, 10f),
            Entry(24f, 6f),
        ))

        _pieChartState.value = PieChartState.Loading
        _pieChartState.value = PieChartState.Success(listOf(
//            PieEntry(0f, "Ужасное"),
            PieEntry(2f, "Плохое", 2),
            PieEntry(35f, "Нормальное", 3),
            PieEntry(18f, "Хорошее",4),
            PieEntry(45f, "Замечательное", 5)
        ))
    }

    private fun getYearStatistic() {
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(mutableListOf())

        _pieChartState.value = PieChartState.Loading
        _pieChartState.value = PieChartState.Success(mutableListOf())
    }


}