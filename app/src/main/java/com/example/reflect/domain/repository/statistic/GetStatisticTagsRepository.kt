package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticTagModel

interface GetStatisticTagsRepository {
    suspend fun getStatisticTags(startDate: String, endDate: String): List<StatisticTagModel>
}