package com.example.reflect.domain.repository.statistic

import com.example.reflect.domain.model.StatisticTagModel

interface GetStatisticEmotionalTagsRepository {
    suspend fun getStatisticEmotionalTags(startDate: String, endDate: String): List<StatisticTagModel>
}