package com.example.reflect.data.repository

import com.example.reflect.data.dto.RegistrationRequestDTO
import com.example.reflect.data.dto.RegistrationResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.repository.RegistrationRepository
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
            throw Exception("Code: ${response.code()}, Message: ${response.message()}, Details: ${response.errorBody()}")
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