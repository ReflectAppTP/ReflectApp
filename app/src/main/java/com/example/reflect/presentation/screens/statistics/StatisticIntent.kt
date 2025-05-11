package com.example.reflect.presentation.screens.statistics

sealed class StatisticIntent {
    data object WeekStatistic: StatisticIntent()
    data object MonthStatistic: StatisticIntent()
    data object YearStatistic: StatisticIntent()
}