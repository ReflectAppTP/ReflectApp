package com.example.reflect.data.repository.statistic

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.statistic.StatisticMoodResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.StatisticMoodModel
import com.example.reflect.domain.repository.statistic.GetStateFrequencyRepository
import javax.inject.Inject

class GetStateFrequencyRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetStateFrequencyRepository {
    override suspend fun getStateFrequency(
        startDate: String,
        endDate: String
    ): List<StatisticMoodModel> {
        val response = remoteData.getStateFrequency(startDate, endDate)
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun StatisticMoodResponseDTO.toDomain() = StatisticMoodModel(
        state = this.state,
        freq = this.freq
    )

    private fun List<StatisticMoodResponseDTO>.toDomain() = this.map { it.toDomain() }
}