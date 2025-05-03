package com.example.reflect.data.remote.api

import com.example.reflect.data.dto.login.LoginRequestDTO
import com.example.reflect.data.dto.login.LoginResponseDTO
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.dto.registration.RegistrationResponseDTO
import com.example.reflect.data.dto.UserDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val authReflect = "api/authReflect"
private const val token = "api/token"

interface RetrofitService {

    @POST("${authReflect}/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequestDTO): Response<RegistrationResponseDTO>

    @POST("${authReflect}/login/")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @GET("${authReflect}/profile/")
    suspend fun getProfile(@Header("Authorization") accessToken: String): Response<UserDTO>

    @GET("${token}/refresh")
    suspend fun getAccessToken(@Body refreshRequestDTO: RefreshRequestDTO): Response<LoginResponseDTO>
}