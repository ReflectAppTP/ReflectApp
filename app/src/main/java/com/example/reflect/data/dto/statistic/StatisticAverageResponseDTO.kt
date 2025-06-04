package com.example.reflect.data.dto.statistic

import com.google.gson.annotations.SerializedName

data class StatisticAverageResponseDTO(
    @SerializedName("date") val date: String,
    @SerializedName("average_mood") val averageMood: Float
)