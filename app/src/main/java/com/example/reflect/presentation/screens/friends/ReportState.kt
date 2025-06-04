package com.example.reflect.presentation.screens.friends

sealed class ReportState {
    data object Idle: ReportState()
    data object Loading: ReportState()
    data object SuccessUser: ReportState()
    data object SuccessState: ReportState()
    data class Error(val message: String): ReportState()
}