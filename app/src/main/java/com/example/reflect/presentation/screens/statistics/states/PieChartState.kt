package com.example.reflect.presentation.screens.statistics.states

import com.github.mikephil.charting.data.PieEntry

sealed class PieChartState {
    data object Idle: PieChartState()
    data object Loading: PieChartState()
    data class Success(val data: List<PieEntry>): PieChartState()
    data class Error(val message: String): PieChartState()
}