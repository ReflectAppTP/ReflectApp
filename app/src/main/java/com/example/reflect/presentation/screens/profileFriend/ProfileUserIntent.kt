package com.example.reflect.presentation.screens.profileFriend

sealed class ProfileUserIntent {
    data object FriendRequest: ProfileUserIntent()
    data class SendReport(val id: Int, val report: String): ProfileUserIntent()
}