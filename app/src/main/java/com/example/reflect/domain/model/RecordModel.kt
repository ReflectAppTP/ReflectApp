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
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecordModel) return false
        return id == other.id && value == other.value && firstTagList == other.firstTagList &&
                secondTagList == other.secondTagList && description == other.description &&
                creationDate == other.creationDate
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + value
        result = 31 * result + (firstTagList?.hashCode() ?: 0)
        result = 31 * result + (secondTagList?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (creationDate?.hashCode() ?: 0)
        return result
    }
    }

