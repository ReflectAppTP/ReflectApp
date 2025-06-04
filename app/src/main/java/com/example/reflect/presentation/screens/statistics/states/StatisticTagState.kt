package com.example.reflect.presentation.screens.statistics.states

import com.example.reflect.domain.model.StatisticTagModel

sealed class StatisticTagState {
    data object Idle: StatisticTagState()
    data object Loading: StatisticTagState()
    data class Success(val data: List<StatisticTagModel>): StatisticTagState()
    data class Error(val message: String): StatisticTagState()
}