package com.example.reflect.data.remote.api

import com.example.reflect.data.dto.GetUserByIdDTO
import com.example.reflect.data.dto.StateRequestDTO
import com.example.reflect.data.dto.StateResponseDTO
import com.example.reflect.data.dto.TagDTO
import com.example.reflect.data.dto.login.LoginRequestDTO
import com.example.reflect.data.dto.login.LoginResponseDTO
import com.example.reflect.data.dto.registration.RegistrationRequestDTO
import com.example.reflect.data.dto.registration.RegistrationResponseDTO
import com.example.reflect.data.dto.UserDTO
import com.example.reflect.data.dto.ai.AIGetMessageResponseDTO
import com.example.reflect.data.dto.ai.AISendMessageRequestDTO
import com.example.reflect.data.dto.ai.AISendMessageResponseDTO
import com.example.reflect.data.dto.friendship.GetFriendsResponseDTO
import com.example.reflect.data.dto.login.RefreshRequestDTO
import com.example.reflect.data.dto.statistic.StatisticAverageResponseDTO
import com.example.reflect.data.dto.statistic.StatisticMoodResponseDTO
import com.example.reflect.data.dto.statistic.StatisticTagResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val authReflect = "api/authReflect"
private const val token = "api/token"
private const val emotions = "api/emotions"
private const val statistic = "api/emotions/statistics"
private const val ai = "api/ai/chat"
private const val friends = "api/friends"
private const val profile = "api/profileReflect"

interface RetrofitService {

    @POST("$authReflect/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequestDTO): Response<RegistrationResponseDTO>

    @POST("$authReflect/login/")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @GET("$authReflect/profile/")
    suspend fun getProfile(): Response<UserDTO>

    @POST("$token/refresh/")
    suspend fun getAccessToken(@Body refreshRequestDTO: RefreshRequestDTO): Response<LoginResponseDTO>

    @GET("$emotions/tags")
    suspend fun getFirstTags(): Response<List<TagDTO>>

    @GET("$emotions/emotional-tags")
    suspend fun getSecondTags(): Response<List<TagDTO>>

    @POST("$emotions/states/")
    suspend fun addState(@Body stateRequestDTO: StateRequestDTO): Response<StateResponseDTO>

    @GET("$emotions/states/")
    suspend fun getStates(@Query("date") date: String): Response<List<StateResponseDTO>>

    @PATCH("$emotions/states/{id}/")
    suspend fun editState(@Path("id") id: Int, @Body stateRequestDTO: StateRequestDTO): Response<StateResponseDTO>

    @DELETE("$emotions/states/{id}/")
    suspend fun deleteState(@Path("id") id: Int): Response<Unit>

    @GET("$statistic/mood/")
    suspend fun getStateFrequency(@Query("start_date") startDate: String, @Query("end_date") endDate: String): Response<List<StatisticMoodResponseDTO>>

    @GET("$statistic/tags/")
    suspend fun getStatisticTags(@Query("start_date") startDate: String, @Query("end_date") endDate: String): Response<List<StatisticTagResponseDTO>>

    @GET("$statistic/emotional-tags/")
    suspend fun getStatisticEmotionalTags(@Query("start_date") startDate: String, @Query("end_date") endDate: String): Response<List<StatisticTagResponseDTO>>

    @GET("$statistic/mood/weekly/")
    suspend fun getWeeklyAverage(): Response<List<StatisticAverageResponseDTO>>

    @GET("$statistic/mood/monthly/")
    suspend fun getMonthlyAverage(): Response<List<StatisticAverageResponseDTO>>

    @GET("$statistic/mood/yearly/")
    suspend fun getYearlyAverage(): Response<List<StatisticAverageResponseDTO>>

    @POST("$ai/send/")
    suspend fun postMessageToAI(@Body aiMessageDTO: AISendMessageRequestDTO): Response<AISendMessageResponseDTO>

    @POST("$ai/reset/")
    suspend fun resetContext(): Response<Unit>

    @GET("$ai/messages/{message_id}")
    suspend fun getAIMessage(@Path("message_id") messageId: Int): Response<AIGetMessageResponseDTO>

    @GET("$friends/friendships/listf/")
    suspend fun getFriendsList(): Response<List<GetFriendsResponseDTO>>

    @GET("$friends/by-username/{username}/")
    suspend fun searchUsers(@Path("username") username: String): Response<List<GetFriendsResponseDTO>>

    @GET("$profile/user/{id}/")
    suspend fun getUserById(@Path("id") id: Int): Response<GetUserByIdDTO>
}