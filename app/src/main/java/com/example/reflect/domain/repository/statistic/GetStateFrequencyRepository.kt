package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticMoodModel

interface GetStateFrequencyRepository {
    suspend fun getStateFrequency(startDate: String, endDate: String): List<StatisticMoodModel>
}