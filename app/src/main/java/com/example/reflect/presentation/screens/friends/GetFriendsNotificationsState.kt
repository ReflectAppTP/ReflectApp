package com.example.reflect.presentation.screens.friends

import com.example.reflect.domain.model.NotificationFriendshipModel

sealed class GetFriendsNotificationsState(val viewType: Int) {
    data object EmptyContent: GetFriendsNotificationsState(1)
    data object Loading: GetFriendsNotificationsState(2)
    data class Success(val users: List<NotificationFriendshipModel>): GetFriendsNotificationsState(3)
    data class Error(val message: String): GetFriendsNotificationsState(4)
}