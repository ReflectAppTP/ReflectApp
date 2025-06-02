package com.example.reflect.presentation.screens.friends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.friendship.GetFriendsNotificationUseCase
import com.example.reflect.domain.usecase.friendship.WebSocketFriendshipUseCase
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelNotificationFriendship @Inject constructor(
    private val webSocketFriendshipUseCase: WebSocketFriendshipUseCase,
    private val getFriendsNotificationUseCase: GetFriendsNotificationUseCase,
): ViewModel() {
    val notifications = webSocketFriendshipUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private var _notificationState = MutableStateFlow<GetFriendsNotificationsState>(GetFriendsNotificationsState.EmptyContent)
    val notificationState: StateFlow<GetFriendsNotificationsState> = _notificationState

    init {
        getNotifications()
    }

    private fun getNotifications() {
        _notificationState.value = GetFriendsNotificationsState.Loading
        viewModelScope.launch {
            getFriendsNotificationUseCase().collect {
                _notificationState.value = it
            }
        }
//        viewModelScope.launch {
//            notifications.collect {
//                if (it != null) {
//                    _notificationState.value
//                }
//            }
//        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            webSocketFriendshipUseCase.disconnect()
        }
        super.onCleared()
    }
}