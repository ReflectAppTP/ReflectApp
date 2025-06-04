package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class ReportUserRequestDTO(
    @SerializedName("reported_user") val reportedUser: Int,
    @SerializedName("reason") val reason: String
)