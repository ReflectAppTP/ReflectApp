package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticAverageModel

interface GetMonthlyAverageRepository {
    suspend fun getMonthlyAverage(): List<StatisticAverageModel>
}