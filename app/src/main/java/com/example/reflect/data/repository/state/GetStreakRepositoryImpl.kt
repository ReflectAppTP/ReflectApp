package com.example.reflect.data.repository.state

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.state.GetStreakRepository
import javax.inject.Inject

class GetStreakRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetStreakRepository {
    override suspend fun getStreak(): Int {
        val response = remoteData.getStreak()
        if (response.isSuccessful) {
            return response.body()!!.streak
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}