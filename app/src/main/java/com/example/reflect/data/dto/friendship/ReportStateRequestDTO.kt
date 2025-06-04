package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class ReportStateRequestDTO(
    @SerializedName("state") val state: Int,
    @SerializedName("reason") val reason: String
)
