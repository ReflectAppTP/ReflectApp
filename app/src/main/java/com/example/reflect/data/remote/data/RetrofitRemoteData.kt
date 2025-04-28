package com.example.reflect.data.remote.data

import com.example.reflect.data.dto.RegistrationRequestDTO
import com.example.reflect.data.remote.api.RetrofitService
import javax.inject.Inject

class RetrofitRemoteData @Inject constructor(private val retrofitService: RetrofitService){
    suspend fun register(registrationRequest: RegistrationRequestDTO) = retrofitService.register(registrationRequest)
}