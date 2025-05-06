package com.example.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class RecordModel(
    val id: Int,
    val value: Int,
    val firstTagList: List<TagModel>?,
    val secondTagList: List<TagModel>?,
    val description: String?,
    val creationDate: Date?
    ): Parcelable