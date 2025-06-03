package com.example.reflect.data.repository.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.friendship.ReportStateRequestDTO
import com.example.reflect.data.dto.friendship.ReportStateResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.ReportStateModel
import com.example.reflect.domain.repository.friendship.ReportStateRepository
import javax.inject.Inject

class ReportStateRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : ReportStateRepository {
    override suspend fun reportState(reportedUser: Int, reason: String): ReportStateModel {
        val response = remoteData.reportState(
            ReportStateRequestDTO(reportedUser, reason)
        )
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun ReportStateResponseDTO.toDomain() = ReportStateModel(
        id, reason, createdAt, isResolved, isAccepted, reporter, reportedUser
    )
}