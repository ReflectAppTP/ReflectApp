package com.example.reflect.domain.model

import android.os.Parcelable
import com.example.reflect.common.FriendshipEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetUserByIdModel(
    val id: Int,
    val username: String,
    val friendshipStatus: FriendshipEnum,
    val lastState: RecordModel? = null,
    val week: List<StatisticAverageModel>? = null
): Parcelable