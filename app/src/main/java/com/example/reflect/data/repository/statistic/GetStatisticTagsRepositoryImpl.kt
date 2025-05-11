package com.example.reflect.data.repository.statistic

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.statistic.StatisticTagResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.StatisticTagModel
import com.example.reflect.domain.repository.statistic.GetStatisticTagsRepository
import javax.inject.Inject

class GetStatisticTagsRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetStatisticTagsRepository {
    override suspend fun getStatisticTags(
        startDate: String,
        endDate: String
    ): List<StatisticTagModel> {
        val response = remoteData.getStatisticTags(startDate, endDate)
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun StatisticTagResponseDTO.toDoamin() = StatisticTagModel(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        freq = this.freq
    )

    private fun List<StatisticTagResponseDTO>.toDomain() = this.map { it.toDoamin() }
}