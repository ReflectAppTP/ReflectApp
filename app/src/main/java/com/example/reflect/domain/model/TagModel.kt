package com.example.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagModel(
    val id: Int,
    val name: String,
    val emoji: String?
): Parcelable