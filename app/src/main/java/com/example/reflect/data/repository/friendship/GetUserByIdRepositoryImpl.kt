package com.example.reflect.data.repository.friendship

import com.example.reflect.common.FriendshipEnum
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.GetUserByIdDTO
import com.example.reflect.data.dto.StateResponseDTO
import com.example.reflect.data.dto.statistic.StatisticAverageResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.GetUserByIdModel
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.domain.repository.friendship.GetUserByIdRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class GetUserByIdRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : GetUserByIdRepository {
    override suspend fun getUserById(id: Int): GetUserByIdModel {
        val response = remoteData.getUserById(id)
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun GetUserByIdDTO.toDomain() = GetUserByIdModel(
        id = this.id,
        username = this.username,
        friendshipStatus = when (this.friendshipStatus) {
            "not_friend" -> FriendshipEnum.User
            "friend" -> FriendshipEnum.Friend
            else -> FriendshipEnum.Banned
        },
        lastState = this.lastState?.toDomain(),
        week = this.week?.toDomain()

    )

    private fun StateResponseDTO.toDomain() = RecordModel(
        id = this.id,
        value = this.value,
        firstTagList = this.firstTags,
        secondTagList = this.secondTags,
        description = this.description,
        creationDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).also {
            it.timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this.createdAt)
    )

    private fun StatisticAverageResponseDTO.toDomain() = StatisticAverageModel(
        date = this.date,
        averageMood = this.averageMood
    )

    private fun List<StatisticAverageResponseDTO>.toDomain() = this.map { it.toDomain() }
}