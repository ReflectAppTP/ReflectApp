package com.example.reflect.data.dto.login

import com.google.gson.annotations.SerializedName

data class GuestDTO(
    @SerializedName("access") val access: String,
    @SerializedName("refresh") val refresh: String,
    @SerializedName("username") val username: String,
    @SerializedName("is_guest") val isGuest: Boolean
)
