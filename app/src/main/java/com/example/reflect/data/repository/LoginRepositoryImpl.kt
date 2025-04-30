package com.example.reflect.data.repository

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.LoginRequestDTO
import com.example.reflect.data.dto.LoginResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.LoginModel
import com.example.reflect.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : LoginRepository {
    override suspend fun login(email: String, password: String): LoginModel {
        val response = remoteData.login(
            LoginRequestDTO(email, password)
        )
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