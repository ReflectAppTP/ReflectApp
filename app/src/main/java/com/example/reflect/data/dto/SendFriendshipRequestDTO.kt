package com.example.reflect.data.dto

import com.example.reflect.data.dto.friendship.GetFriendsResponseDTO
import com.google.gson.annotations.SerializedName

data class SendFriendshipRequestDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("from_user") val fromUser: GetFriendsResponseDTO,
    @SerializedName("to_user") val toUser: GetFriendsResponseDTO,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
)