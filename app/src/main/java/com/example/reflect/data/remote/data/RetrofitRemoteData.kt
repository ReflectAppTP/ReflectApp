package com.example.reflect.data.remote.data

import com.example.reflect.data.dto.ChangeLoginDTO
import com.example.reflect.data.dto.ChangePasswordDTO
import com.example.reflect.data.dto.ChangeVisibilityDTO
import com.example.reflect.data.dto.StateRequestDTO
import com.example.reflect.data.dto.UserIdDTO
import com.example.reflect.data.dto.ai.AISendMessageRequestDTO
import com.example.reflect.data.dto.friendship.ReportStateRequestDTO
import com.example.reflect.data.dto.friendship.ReportUserRequestDTO
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
    suspend fun addState(stateRequestDTO: StateRequestDTO) = retrofitService.addState(stateRequestDTO)
    suspend fun getStates(date: String) = retrofitService.getStates(date)
    suspend fun editState(id: Int, stateRequestDTO: StateRequestDTO) = retrofitService.editState(id, stateRequestDTO)
    suspend fun deleteState(id: Int) = retrofitService.deleteState(id)

    suspend fun getStateFrequency(startDate: String, endDate: String) = retrofitService.getStateFrequency(startDate, endDate)
    suspend fun getStatisticTags(startDate: String, endDate: String) = retrofitService.getStatisticTags(startDate, endDate)
    suspend fun getStatisticEmotionalTags(startDate: String, endDate: String) = retrofitService.getStatisticEmotionalTags(startDate, endDate)
    suspend fun getWeeklyAverage() = retrofitService.getWeeklyAverage()
    suspend fun getMonthlyAverage() = retrofitService.getMonthlyAverage()
    suspend fun getYearlyAverage() = retrofitService.getYearlyAverage()

    suspend fun postMessageToAI(aiMessageDTO: AISendMessageRequestDTO) = retrofitService.postMessageToAI(aiMessageDTO)
    suspend fun resetContext() = retrofitService.resetContext()
    suspend fun getAIMessage(messageId: Int) = retrofitService.getAIMessage(messageId)

    suspend fun getFriendsList() = retrofitService.getFriendsList()
    suspend fun searchUsers(username: String) = retrofitService.searchUsers(username)
    suspend fun sendFriendshipRequest(userIdDTO: UserIdDTO) = retrofitService.sendFriendshipRequest(userIdDTO)
    suspend fun getFriendshipNotification() = retrofitService.getFriendshipNotification()
    suspend fun acceptFriendship(id: Int) = retrofitService.acceptFriendship(id)
    suspend fun rejectFriendship(id: Int) = retrofitService.rejectFriendship(id)
    suspend fun getStreak() = retrofitService.getStreak()

    suspend fun getUserById(id: Int) = retrofitService.getUserById(id)

    suspend fun reportUser(reportDTO: ReportUserRequestDTO) = retrofitService.reportUser(reportDTO)
    suspend fun reportState(reportDTO: ReportStateRequestDTO) = retrofitService.reportState(reportDTO)

    suspend fun updatePassword(changePasswordDTO: ChangePasswordDTO) = retrofitService.updatePassword(changePasswordDTO)
    suspend fun updateUsername(changeLoginDTO: ChangeLoginDTO) = retrofitService.updateUsername(changeLoginDTO)
    suspend fun updateVisibility(changeVisibilityDTO: ChangeVisibilityDTO) = retrofitService.updateVisibility(changeVisibilityDTO)
}