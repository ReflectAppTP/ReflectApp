package com.example.reflect.data.dto

import com.example.reflect.data.dto.statistic.StatisticAverageResponseDTO
import com.google.gson.annotations.SerializedName

data class GetUserByIdDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("friendship_status") val friendshipStatus: String,
    @SerializedName("is_premium") val isPremium: Boolean,
    @SerializedName("visibility") val visibility: String,
    @SerializedName("last_state") val lastState: StateResponseDTO? = null,
    @SerializedName("week") val week: List<StatisticAverageResponseDTO>? = null,
)