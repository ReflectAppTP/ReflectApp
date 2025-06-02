package com.example.reflect.presentation.screens.profileFriend

sealed class ProfileUserIntent {
    data object FriendRequest: ProfileUserIntent()
    data object SendReport: ProfileUserIntent()
}