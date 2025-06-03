package com.example.reflect.domain.repository.friendship

import com.example.reflect.domain.model.NotificationFriendshipModel

interface GetFriendsNotificationRepository {
    suspend fun getFriendshipNotification(): MutableList<NotificationFriendshipModel>
}