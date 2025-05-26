package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.GetFriendModel

interface GetFriendsListRepository {
    suspend fun getFriendList(): List<GetFriendModel>
}