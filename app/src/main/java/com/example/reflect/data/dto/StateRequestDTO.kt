package com.example.reflect.data.dto

import com.google.gson.annotations.SerializedName

data class StateRequestDTO(
    @SerializedName("value") val value: Int,
    @SerializedName("description") val description: String,
    @SerializedName("tag_ids") val firstTagsIndices: List<Int>,
    @SerializedName("emotional_tag_ids") val secondTagsIndices: List<Int>
)