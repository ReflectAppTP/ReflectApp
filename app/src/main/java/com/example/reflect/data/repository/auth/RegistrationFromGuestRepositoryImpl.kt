package com.example.reflect.data.repository.auth

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.repository.auth.RegistrationFromGuestRepository
import javax.inject.Inject

class RegistrationFromGuestRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): RegistrationFromGuestRepository {
    override suspend fun registerFromGuest(
        username: String,
        email: String,
        password: String
    ): UserModel {
        val response = remoteData.registerFromGuest(
            RegistrationRequestDTO(username, email, password)
        )
        if (response.isSuccessful) {
            Log.d("OkHttp", "Регистрация с гостя прошла успешно, спасибо Олегу")
            // TODO: ГОВНОКОД 
            return UserModel(-1,"", "","", isAdmin = false, isPremium = false)
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

}