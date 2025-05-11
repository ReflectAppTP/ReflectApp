package com.example.reflect.data.repository.auth

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.dto.registration.RegistrationResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.repository.auth.RegistrationRepository
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : RegistrationRepository {
    override suspend fun register(username: String, email: String, password: String): UserModel {
        val response = remoteData.register(
            RegistrationRequestDTO(username, email, password)
        )
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun RegistrationResponseDTO.toDomain() = UserModel(
        id = this.user.id,
        username = this.user.username,
        email = this.user.username,
        createdAt = this.user.createdAt,
        isAdmin = this.user.isAdmin,
        isPremium = this.user.isPremium
    )
}