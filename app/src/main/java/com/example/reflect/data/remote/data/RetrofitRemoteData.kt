package com.example.reflect.data.remote.data

import com.example.reflect.data.dto.StateRequestDTO
import com.example.reflect.data.dto.login.LoginRequestDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.remote.api.RetrofitService
import java.util.Date
import javax.inject.Inject

class RetrofitRemoteData @Inject constructor(private val retrofitService: RetrofitService){
    suspend fun register(registrationRequest: RegistrationRequestDTO) = retrofitService.register(registrationRequest)
    suspend fun login(loginRequest: LoginRequestDTO) = retrofitService.login(loginRequest)
    suspend fun getUser() = retrofitService.getProfile()
    suspend fun getAccessToken(refreshToken: RefreshRequestDTO) = retrofitService.getAccessToken(refreshToken)

    suspend fun getFirstTags() = retrofitService.getFirstTags()
    suspend fun getSecondTags() = retrofitService.getSecondTags()
    suspend fun addState(stateRequestDTO: StateRequestDTO) = retrofitService.addState(stateRequestDTO)
    suspend fun getStates(date: String) = retrofitService.getStates(date)
    suspend fun editState(id: Int, stateRequestDTO: StateRequestDTO) = retrofitService.editState(id, stateRequestDTO)
    suspend fun deleteState(id: Int) = retrofitService.deleteState(id)

    suspend fun getStateFrequency(startDate: String, endDate: String) = retrofitService.getStateFrequency(startDate, endDate)
    suspend fun getStatisticTags(startDate: String, endDate: String) = retrofitService.getStatisticTags(startDate, endDate)
    suspend fun getStatisticEmotionalTags(startDate: String, endDate: String) = retrofitService.getStatisticEmotionalTags(startDate, endDate)
    suspend fun getWeeklyAverage() = retrofitService.getWeeklyAverage()
}