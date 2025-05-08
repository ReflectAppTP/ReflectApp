package com.example.reflect.data.dto

import com.example.reflect.domain.model.TagModel
import com.google.gson.annotations.SerializedName

data class StateResponseDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("value") val value: Int,
    @SerializedName("description") val description: String,
    @SerializedName("tags") val firstTags: List<TagModel>,
    @SerializedName("emotional_tags") val secondTags: List<TagModel>,
    @SerializedName("created_at") val createdAt: String
)