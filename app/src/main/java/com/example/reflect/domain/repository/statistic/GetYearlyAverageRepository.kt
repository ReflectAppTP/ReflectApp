package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticAverageModel

interface GetYearlyAverageRepository {
    suspend fun getYearlyAverage(): List<StatisticAverageModel>
}