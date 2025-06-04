package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class ChangePremiumDTO(
    @SerializedName("is_premium") val isPremium: Boolean
)
