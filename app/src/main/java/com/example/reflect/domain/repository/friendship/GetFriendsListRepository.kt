package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.GetShortUserModel

interface GetFriendsListRepository {
    suspend fun getFriendList(): List<GetShortUserModel>
}