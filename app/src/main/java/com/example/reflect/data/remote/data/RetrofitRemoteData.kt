package com.example.reflect.data.remote.data

import com.example.reflect.data.dto.LoginRequestDTO
import com.example.reflect.data.dto.RegistrationRequestDTO
import com.example.reflect.data.remote.api.RetrofitService
import javax.inject.Inject

class RetrofitRemoteData @Inject constructor(private val retrofitService: RetrofitService){
    suspend fun register(registrationRequest: RegistrationRequestDTO) = retrofitService.register(registrationRequest)
    suspend fun login(loginRequest: LoginRequestDTO) = retrofitService.login(loginRequest)
    suspend fun getUser(accessToken: String) = retrofitService.getProfile(accessToken)
}