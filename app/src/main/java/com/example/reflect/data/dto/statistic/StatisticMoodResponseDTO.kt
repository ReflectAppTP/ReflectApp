package com.example.reflect.data.dto.statistic

import com.google.gson.annotations.SerializedName

data class StatisticMoodResponseDTO(
    @SerializedName("state") val state: Int,
    @SerializedName("freq") val freq: Int
)