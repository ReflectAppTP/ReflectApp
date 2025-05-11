package com.example.reflect.data.repository.statistic

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.statistic.StatisticAverageResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.domain.repository.statistic.GetMonthlyAverageRepository
import javax.inject.Inject

class GetMonthlyAverageRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : GetMonthlyAverageRepository {
    override suspend fun getMonthlyAverage(): List<StatisticAverageModel> {
        val response = remoteData.getMonthlyAverage()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun StatisticAverageResponseDTO.toDomain() = StatisticAverageModel(
        date = this.date,
        averageMood = this.averageMood
    )

    private fun List<StatisticAverageResponseDTO>.toDomain() = this.map { it.toDomain() }
}