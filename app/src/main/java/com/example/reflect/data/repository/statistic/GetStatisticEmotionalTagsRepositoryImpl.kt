package com.example.reflect.data.repository.statistic

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.statistic.StatisticTagResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.StatisticTagModel
import com.example.reflect.domain.repository.statistic.GetStatisticEmotionalTagsRepository
import javax.inject.Inject

class GetStatisticEmotionalTagsRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetStatisticEmotionalTagsRepository {
    override suspend fun getStatisticEmotionalTags(
        startDate: String,
        endDate: String
    ): List<StatisticTagModel> {
        val response = remoteData.getStatisticEmotionalTags(startDate, endDate)
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
    private fun StatisticTagResponseDTO.toDomain() = StatisticTagModel(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        freq = this.freq
    )

    private fun List<StatisticTagResponseDTO>.toDomain() = this.map { it.toDomain() }
}