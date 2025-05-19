package com.example.reflect.presentation.screens.friends.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.UserModel
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import com.example.reflect.presentation.screens.friends.GetFriendsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelFriends @Inject constructor(): ViewModel() {

    private var _friendsListState = MutableStateFlow<GetFriendsState>(GetFriendsState.EmptyContent)
    val friendsListState: StateFlow<GetFriendsState> = _friendsListState

    private var _friendsNotificationListState = MutableStateFlow<GetFriendsNotificationsState>(GetFriendsNotificationsState.EmptyContent)
    val friendsNotificationListState: StateFlow<GetFriendsNotificationsState> = _friendsNotificationListState

    fun fetchFriends() {
        _friendsListState.value = GetFriendsState.Success(mutableListOf(
            UserModel(1,"oleg", "fasdfsa", "sgsd", false, true),
            UserModel(1,"мяумуяделюксфыва", "fasdfsa", "sgsd", false, true),
            UserModel(1,"фываыфваыфваыфваы", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
        ))
    }

    fun getUser(user: UserModel) {
        Log.d("OK", "проверка работы клика")
    }

    // TODO: Переделать на сокетах
    fun fetchNotifications() {
        _friendsNotificationListState.value = GetFriendsNotificationsState.Success(mutableListOf(
            UserModel(1,"oleg", "fasdfsa", "sgsd", false, true),
            UserModel(1,"мяумуяделюксфыва", "fasdfsa", "sgsd", false, true),
            UserModel(1,"фываыфваыфваыфваы", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
        ))
    }

    fun acceptFriendRequest(id: Int) {

    }

    fun declineFriendRequest(id: Int) {

    }
}