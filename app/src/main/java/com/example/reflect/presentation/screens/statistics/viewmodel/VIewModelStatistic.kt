package com.example.reflect.presentation.screens.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.LineChartState
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
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
            BarEntry(0f, 9f),
            BarEntry(1f, 7f),
            BarEntry(2f, 10f),
            BarEntry(3f, 6f),
            BarEntry(4f, 8f),
            BarEntry(5f, 4f),
            BarEntry(6f, 2f)
        ))
    }

    private fun getMonthStatistic() {
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(listOf(BarEntry(0f, 1f), BarEntry(2f, 10f),BarEntry(5f, 4f)))
    }

    private fun getYearStatistic() {
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(mutableListOf())
    }
}