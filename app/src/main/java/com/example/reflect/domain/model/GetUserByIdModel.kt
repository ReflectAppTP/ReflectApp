package com.example.reflect.domain.model

import android.os.Parcelable
import com.example.reflect.common.FriendshipEnum
import com.example.reflect.common.UserVisibilityEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetUserByIdModel(
    val id: Int,
    val username: String,
    val friendshipStatus: FriendshipEnum,
    val isPremium: Boolean,
    val visibility: UserVisibilityEnum,
    val lastState: RecordModel? = null,
    val week: List<StatisticAverageModel>? = null
): Parcelable