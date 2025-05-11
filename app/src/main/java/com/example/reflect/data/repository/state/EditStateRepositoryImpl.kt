package com.example.reflect.data.repository.state

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.StateRequestDTO
import com.example.reflect.data.dto.StateResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.repository.state.EditStateRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class EditStateRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): EditStateRepository {
    override suspend fun editState(id: Int, value: Int, description: String, firstTagsIndices: List<Int>, secondTagsIndices: List<Int>): RecordModel {
        val response = remoteData.editState(id, StateRequestDTO(value, description, firstTagsIndices, secondTagsIndices))
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun StateResponseDTO.toDomain() = RecordModel(
        id = this.id,
        value = this.value,
        firstTagList = this.firstTags,
        secondTagList = this.secondTags,
        description = this.description,
        creationDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).also {
            it.timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this.createdAt)
    )
}