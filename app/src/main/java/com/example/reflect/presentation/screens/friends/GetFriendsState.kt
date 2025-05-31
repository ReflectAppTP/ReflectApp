package com.example.reflect.presentation.screens.friends

import com.example.reflect.domain.model.GetShortUserModel

sealed class GetFriendsState(val viewType: Int) {
    data object EmptyContent: GetFriendsState(1)
    data object Loading: GetFriendsState(2)
    data class Success(val friends: List<GetShortUserModel>): GetFriendsState(3)
    data class Error(val message: String): GetFriendsState(4)
}