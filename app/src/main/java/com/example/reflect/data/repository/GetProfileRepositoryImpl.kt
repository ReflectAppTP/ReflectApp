package com.example.reflect.data.repository

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.UserDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.repository.GetProfileRepository
import javax.inject.Inject

class GetProfileRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetProfileRepository {
    override suspend fun getUser(): UserModel {
        val response = remoteData.getUser()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun UserDTO.toDomain() = UserModel(
        id = this.id,
        username = this.username,
        email = this.username,
        createdAt = this.createdAt,
        isAdmin = this.isAdmin,
        isPremium = this.isPremium
    )
}
