package com.example.reflect.domain.model

import java.util.Date

data class RecordModel(
    val id: Int,
    val value: Int,
    val firstTagList: List<TagModel>?,
    val secondTagList: List<TagModel>?,
    val description: String?,
    val creationDate: Date
    )