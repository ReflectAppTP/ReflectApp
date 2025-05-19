package com.example.reflect.presentation.screens.friends.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.UserModel
import com.example.reflect.presentation.screens.friends.SearchFriendsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelSearchFriends @Inject constructor() : ViewModel()  {

    private var _searchUsersState = MutableStateFlow<SearchFriendsState>(SearchFriendsState.EmptyContent)
    val searchUsersState: StateFlow<SearchFriendsState> = _searchUsersState

    fun searchUsers(query: String) {
        if (query == "oleg") {
            _searchUsersState.value = SearchFriendsState.Success(mutableListOf(
                UserModel(1,"oleg", "fasdfsa", "sgsd", false, true),
            ))
        } else {
            _searchUsersState.value = SearchFriendsState.Success(mutableListOf(
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
    }
}