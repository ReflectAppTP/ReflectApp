package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.ReportStateModel

interface ReportStateRepository {
    suspend fun reportState(reportedUser: Int, reason: String): ReportStateModel
}