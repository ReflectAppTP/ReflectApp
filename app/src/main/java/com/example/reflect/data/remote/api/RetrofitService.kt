package com.example.reflect.data.remote.api

import com.example.reflect.data.dto.LoginRequestDTO
import com.example.reflect.data.dto.LoginResponseDTO
import com.example.reflect.data.dto.RegistrationRequestDTO
import com.example.reflect.data.dto.RegistrationResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("api/authReflect/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequestDTO): Response<RegistrationResponseDTO>

    @POST("api/authReflect/login/")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>
}