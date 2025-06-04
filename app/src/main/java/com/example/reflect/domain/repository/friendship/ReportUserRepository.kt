package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.ReportUserModel

interface ReportUserRepository {
    suspend fun reposrtUser(reportedUser: Int, reason: String): ReportUserModel
}