package com.example.reflect.presentation.screens.statistics.states

import com.github.mikephil.charting.data.Entry

sealed class LineChartState {
    data object Idle: LineChartState()
    data object Loading: LineChartState()
    data class Success(val data: List<Entry>): LineChartState()
    data class Error(val message: String): LineChartState()
}