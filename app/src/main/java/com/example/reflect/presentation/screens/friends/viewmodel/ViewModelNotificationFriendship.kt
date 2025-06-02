package com.example.reflect.presentation.screens.friends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.friendship.WebSocketFriendshipUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelNotificationFriendship @Inject constructor(
    private val webSocketFriendshipUseCase: WebSocketFriendshipUseCase
): ViewModel() {
    val notifications = webSocketFriendshipUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    override fun onCleared() {
        viewModelScope.launch {
            webSocketFriendshipUseCase.disconnect()
        }
        super.onCleared()
    }
}