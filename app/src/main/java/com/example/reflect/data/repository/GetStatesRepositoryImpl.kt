package com.example.reflect.data.repository

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.AddStateResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.repository.GetStatesRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class GetStatesRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : GetStatesRepository {
    override suspend fun getStates(): List<RecordModel> {
        val response = remoteData.getStates()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun AddStateResponseDTO.toDomain() = RecordModel(
        id = this.id,
        value = this.value,
        firstTagList = this.firstTags,
        secondTagList = this.secondTags,
        description = this.description,
        creationDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).also {
            it.timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this.createdAt)
    )
    private fun List<AddStateResponseDTO>.toDomain() = this.map { it.toDomain() }
}