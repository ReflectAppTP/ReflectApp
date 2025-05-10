package com.example.reflect.presentation.screens.statistics.states

import com.github.mikephil.charting.data.BarEntry

sealed class BarChartState {
    data object Idle: BarChartState()
    data object Loading: BarChartState()
    data class Success(val data: List<BarEntry>): BarChartState()
    data class Error(val message: String): BarChartState()
}