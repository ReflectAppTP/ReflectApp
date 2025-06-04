package com.example.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticAverageModel(
    val date: String,
    val averageMood: Float
): Parcelable