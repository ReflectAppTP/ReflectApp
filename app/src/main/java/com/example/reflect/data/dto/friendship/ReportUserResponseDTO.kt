package com.example.reflect.data.dto.friendship

import com.google.gson.annotations.SerializedName

data class ReportUserResponseDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("reason") val reason: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_resolved") val isResolved: Boolean,
    @SerializedName("is_accepted") val isAccepted: Boolean?,
    @SerializedName("reporter") val reporter: Int,
    @SerializedName("state") val state: Int
)