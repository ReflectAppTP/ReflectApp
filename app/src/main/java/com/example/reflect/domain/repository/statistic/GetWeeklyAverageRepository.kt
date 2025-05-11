package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticAverageModel

interface GetWeeklyAverageRepository {
    suspend fun getWeeklyAverage(): List<StatisticAverageModel>
}