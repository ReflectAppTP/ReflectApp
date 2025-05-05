package com.example.reflect.data.remote.data

import com.example.reflect.data.dto.AddStateRequestDTO
import com.example.reflect.data.dto.login.LoginRequestDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.remote.api.RetrofitService
import javax.inject.Inject

class RetrofitRemoteData @Inject constructor(private val retrofitService: RetrofitService){
    suspend fun register(registrationRequest: RegistrationRequestDTO) = retrofitService.register(registrationRequest)
    suspend fun login(loginRequest: LoginRequestDTO) = retrofitService.login(loginRequest)
    suspend fun getUser() = retrofitService.getProfile()
    suspend fun getAccessToken(refreshToken: RefreshRequestDTO) = retrofitService.getAccessToken(refreshToken)
    suspend fun getFirstTags() = retrofitService.getFirstTags()
    suspend fun getSecondTags() = retrofitService.getSecondTags()
    suspend fun addState(addStateRequestDTO: AddStateRequestDTO) = retrofitService.addState(addStateRequestDTO)
}