package com.example.reflect.data.repository.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.data.dto.friendship.GetFriendsResponseDTO
import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.domain.model.GetFriendModel
import com.example.reflect.domain.repository.friendship.GetFriendsListRepository
import javax.inject.Inject

class GetFriendsListRepositoryImpl @Inject constructor(
    private val remoteData: RetrofitRemoteData
): GetFriendsListRepository {
    override suspend fun getFriendList(): List<GetFriendModel> {
        val response = remoteData.getFriendsList()
        if (response.isSuccessful) {
            return response.body()!!.toDomain()
        } else {
            throw RetrofitException(response.code(), response.message(), response.errorBody())
        }
    }

    private fun GetFriendsResponseDTO.toDomain() = GetFriendModel(
        id = this.id,
        login = this.username,
        email = this.email,
        isPremium = this.isPremium
    )

    private fun List<GetFriendsResponseDTO>.toDomain() = this.map { it.toDomain() }
}