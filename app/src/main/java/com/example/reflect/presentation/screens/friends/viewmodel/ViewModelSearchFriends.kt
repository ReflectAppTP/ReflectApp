package com.example.reflect.presentation.screens.friends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.friendship.SearchUsersUseCase
import com.example.reflect.presentation.screens.friends.SearchFriendsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSearchFriends @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel()  {

    private var _searchUsersState = MutableStateFlow<SearchFriendsState>(SearchFriendsState.EmptyContent)
    val searchUsersState: StateFlow<SearchFriendsState> = _searchUsersState

    fun searchUsers(query: String) {
        _searchUsersState.value = SearchFriendsState.Loading
        viewModelScope.launch {
            searchUsersUseCase(query).collect {
                _searchUsersState.value = it
            }
        }
    }
}