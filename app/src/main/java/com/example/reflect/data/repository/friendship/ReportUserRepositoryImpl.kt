package com.example.reflect.data.repository.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.friendship.ReportUserRequestDTO
import com.example.reflect.data.dto.friendship.ReportUserResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.ReportUserModel
import com.example.reflect.domain.repository.friendship.ReportUserRepository
import javax.inject.Inject

class ReportUserRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : ReportUserRepository {
    override suspend fun reposrtUser(reportedUser: Int, reason: String): ReportUserModel {
        val response = remoteData.reportUser(
            ReportUserRequestDTO(reportedUser, reason)
        )
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun ReportUserResponseDTO.toDomain() = ReportUserModel(
        id, reason, createdAt, isResolved, isAccepted, reporter, state
    )
}