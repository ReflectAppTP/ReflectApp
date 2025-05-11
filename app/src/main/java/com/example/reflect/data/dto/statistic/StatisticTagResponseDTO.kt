package com.example.reflect.data.dto.statistic

import com.google.gson.annotations.SerializedName

data class StatisticTagResponseDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("emoji") val emoji: String,
    @SerializedName("freq") val freq: Int
)