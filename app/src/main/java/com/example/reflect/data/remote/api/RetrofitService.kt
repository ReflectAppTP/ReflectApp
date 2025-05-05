package com.example.reflect.data.remote.api

import com.example.reflect.data.dto.AddStateRequestDTO
import com.example.reflect.data.dto.AddStateResponseDTO
import com.example.reflect.data.dto.TagDTO
import com.example.reflect.data.dto.login.LoginRequestDTO
import com.example.reflect.data.dto.login.LoginResponseDTO
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.dto.registration.RegistrationResponseDTO
import com.example.reflect.data.dto.UserDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val authReflect = "api/authReflect"
private const val token = "api/token"
private const val emotions = "api/emotions"

interface RetrofitService {

    @POST("${authReflect}/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequestDTO): Response<RegistrationResponseDTO>

    @POST("${authReflect}/login/")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @GET("${authReflect}/profile/")
    suspend fun getProfile(): Response<UserDTO>

    @POST("${token}/refresh")
    suspend fun getAccessToken(@Body refreshRequestDTO: RefreshRequestDTO): Response<LoginResponseDTO>

    @GET("${emotions}/tags")
    suspend fun getFirstTags(): Response<List<TagDTO>>

    @GET("${emotions}/emotional-tags")
    suspend fun getSecondTags(): Response<List<TagDTO>>

    @POST("${emotions}/states/")
    suspend fun addState(@Body addStateRequestDTO: AddStateRequestDTO): Response<AddStateResponseDTO>

    @GET("${emotions}/states/")
    suspend fun getStates(): Response<List<AddStateResponseDTO>>
}