package com.example.reflect.presentation.screens.friends

sealed class SendFriendshipRequestState {
    data object Idle: SendFriendshipRequestState()
    data object Loading: SendFriendshipRequestState()
    data object Success: SendFriendshipRequestState()
    data class Error(val message: String): SendFriendshipRequestState()
}