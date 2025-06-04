package com.example.reflect.data.repository.auth

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.login.GuestDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.repository.auth.LoginLikeGuestRepository
import javax.inject.Inject

class LoginLikeGuestRepositoryImpl @Inject constructor(
    private val retrofitRemoteData: RetrofitRemoteData
): LoginLikeGuestRepository {
    override suspend fun loginLikeGuest(): UserModel {
        val response = retrofitRemoteData.loginLikeGuest()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun GuestDTO.toDomain() = UserModel(
        id = -1,
        username = this.username,
        email = "",
        createdAt = "",
        isAdmin = false,
        isPremium = false,
        isGuest = this.isGuest,
        access = this.access,
        refresh = this.refresh
    )
}