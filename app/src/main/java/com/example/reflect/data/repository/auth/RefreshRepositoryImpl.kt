package com.example.reflect.data.repository.auth

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.login.LoginResponseDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.LoginModel
import com.example.reflect.domain.repository.auth.RefreshRepository
import javax.inject.Inject

class RefreshRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): RefreshRepository {
    override suspend fun getAccessToken(refreshToken: String): LoginModel {
        val response = remoteData.getAccessToken(RefreshRequestDTO(refreshToken))
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun LoginResponseDTO.toDomain() = LoginModel(
        refresh = this.refresh,
        access = this.access
    )
}