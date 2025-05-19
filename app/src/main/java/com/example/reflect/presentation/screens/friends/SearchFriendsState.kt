package com.example.reflect.presentation.screens.friends

import com.example.reflect.domain.model.UserModel

sealed class SearchFriendsState(val viewType: Int) {
    data object EmptyContent: SearchFriendsState(1)
    data object Loading: SearchFriendsState(2)
    data class Success(val users: List<UserModel>): SearchFriendsState(3)
    data class Error(val message: String): SearchFriendsState(4)
}