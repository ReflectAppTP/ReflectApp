package com.example.reflect.data.remote.api

import com.example.reflect.data.dto.LoginRequestDTO
import com.example.reflect.data.dto.LoginResponseDTO
import com.example.reflect.data.dto.RegistrationRequestDTO
import com.example.reflect.data.dto.RegistrationResponseDTO
import com.example.reflect.data.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {

    @POST("api/authReflect/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequestDTO): Response<RegistrationResponseDTO>

    @POST("api/authReflect/login/")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @GET("api/authReflect/profile/")
    suspend fun getProfile(@Header("Authorization") accessToken: String): Response<UserDTO>
}