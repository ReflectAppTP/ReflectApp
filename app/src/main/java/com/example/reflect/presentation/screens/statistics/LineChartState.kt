package com.example.reflect.presentation.screens.statistics

import com.github.mikephil.charting.data.BarEntry

sealed class LineChartState {
    data object Idle: LineChartState()
    data object Loading: LineChartState()
    data class Success(val data: List<BarEntry>): LineChartState()
    data class Error(val message: String): LineChartState()
}