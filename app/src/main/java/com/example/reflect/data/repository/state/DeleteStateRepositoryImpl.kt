package com.example.reflect.data.repository.state

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.repository.state.DeleteStateRepository
import javax.inject.Inject

class DeleteStateRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
) : DeleteStateRepository {
    override suspend fun deleteState(id: Int) {
        val response = remoteData.deleteState(id)
        if (response.isSuccessful) {
            Log.d("OkHttp", "Успешно удалили записиь, спасибо Олегу")
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }
}