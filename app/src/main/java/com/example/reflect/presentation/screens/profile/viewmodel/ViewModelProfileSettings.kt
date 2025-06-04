package com.example.reflect.presentation.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.friendship.ChangeLoginUseCase
import com.example.reflect.domain.usecase.friendship.ChangePasswordUseCase
import com.example.reflect.domain.usecase.friendship.ChangeVisibilityUseCase
import com.example.reflect.presentation.screens.profile.SettingsProfileIntent
import com.example.reflect.presentation.screens.profile.UpdateProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProfileSettings @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val changeLoginUseCase: ChangeLoginUseCase,
    private val changeVisibilityUseCase: ChangeVisibilityUseCase,
): ViewModel() {

    val userIntent = Channel<SettingsProfileIntent>(Channel.UNLIMITED)
    private var _userState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val userState: StateFlow<UpdateProfileState> = _userState

    private var _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private var _oldPassword = MutableStateFlow("")
    val oldPassword: StateFlow<String> = _oldPassword

    private var _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private var _visibility = MutableStateFlow("")
    val visibility: StateFlow<String> = _visibility

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is SettingsProfileIntent.Update -> updateUser(it.username, it.oldPassword, it.newPassword, it.visibility)
                }
            }
        }
    }

    private fun updateUser(
        username: String?,
        oldPassword: String?,
        newPassword: String?,
        visibility: String
    ) {
        viewModelScope.launch {
            if (oldPassword == null || newPassword == null) {
                if (username != null) {
                    changeLoginUseCase(username).collect {
                        if (it is UpdateProfileState.Error) {
                            _userState.value = it
                        }
                    }
                }
                changeVisibilityUseCase(visibility).collect {
//                    _userState.value = it
                }
            } else {
                changePasswordUseCase(oldPassword, newPassword).collect { passwordState ->
                    if (passwordState is UpdateProfileState.Success) {
                        if (username != null) {
                            changeLoginUseCase(username).collect {
                                _userState.value = it
                            }
                        }
                        changeVisibilityUseCase(visibility).collect {
//                            _userState.value = it
                        }
                    } else _userState.value = passwordState
                }
            }
        }
    }

    fun updateLogin(result: String) {
        _login.value = result
    }

    fun updateOldPassword(result: String) {
        _oldPassword.value = result
    }

    fun updateNewPassword(result: String) {
        _newPassword.value = result
    }

    fun updateVisibility(result: String) {
        _visibility.value = result
    }

    fun updateUserState() {
        _userState.value = UpdateProfileState.Idle
    }

    fun isPasswordMoreThanSixSymbols() = _oldPassword.value.length >= 6 && _newPassword.value.length >= 6
}