package com.example.reflect.presentation.screens.friends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.UserModel
import com.example.reflect.domain.usecase.friendship.GetFriendsListUseCase
import com.example.reflect.domain.usecase.friendship.GetUserByIdUseCase
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import com.example.reflect.presentation.screens.friends.GetFriendsState
import com.example.reflect.presentation.screens.friends.fragment.FriendsScreen
import com.example.reflect.presentation.screens.profile.GetUserByIdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelFriends @Inject constructor(
    private val getFriendsListUseCase: GetFriendsListUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
): ViewModel() {

    private var _currentScreen = MutableStateFlow(FriendsScreen.FriendsList)
    val currentScreen: StateFlow<FriendsScreen> = _currentScreen

    private var _friendsListState = MutableStateFlow<GetFriendsState>(GetFriendsState.EmptyContent)
    val friendsListState: StateFlow<GetFriendsState> = _friendsListState

    private var _friendsNotificationListState = MutableStateFlow<GetFriendsNotificationsState>(GetFriendsNotificationsState.EmptyContent)
    val friendsNotificationListState: StateFlow<GetFriendsNotificationsState> = _friendsNotificationListState

    private var _getUserByIdState = MutableStateFlow<GetUserByIdState>(GetUserByIdState.Idle)
    val getUserByIdState: StateFlow<GetUserByIdState> = _getUserByIdState

    private var _userId = MutableStateFlow(-1)
    val userId: StateFlow<Int> = _userId

    init {
        fetchFriends()
    }

    fun fetchFriends() {
        _friendsListState.value = GetFriendsState.EmptyContent
        viewModelScope.launch {
            getFriendsListUseCase().collect { newState ->
                _friendsListState.value = newState
            }
        }
    }

    fun getUser(id: Int) {
        _getUserByIdState.value = GetUserByIdState.Idle
        viewModelScope.launch {
            getUserByIdUseCase(id).collect { newState ->
                _getUserByIdState.value = newState
            }
        }
    }

    // TODO: Переделать на сокетах
    fun fetchNotifications() {
        _friendsNotificationListState.value = GetFriendsNotificationsState.Success(mutableListOf(
            UserModel(1,"oleg", "fasdfsa", "sgsd", false, true),
            UserModel(1,"мяумуяделюксфыва", "fasdfsa", "sgsd", false, false),
            UserModel(1,"фываыфваыфваыфваы", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, false),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, false),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, false),
            UserModel(1,"роман фисташка", "fasdfsa", "sgsd", false, true),
        ))
    }

    fun acceptFriendRequest(id: Int) {

    }

    fun declineFriendRequest(id: Int) {

    }

    fun moveToScreen(screen: FriendsScreen) {
        _currentScreen.value = screen
    }

    fun updateId(id: Int) {
        _userId.value = id
    }

    fun updateUserState() {
        _getUserByIdState.value = GetUserByIdState.Idle
    }
}