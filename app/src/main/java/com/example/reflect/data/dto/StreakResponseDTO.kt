package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class StreakResponseDTO(
    @SerializedName("streak_days") val streak: Int
)
