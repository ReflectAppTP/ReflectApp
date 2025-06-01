package com.example.reflect.presentation.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.presentation.screens.profile.SettingsProfileIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelProfileSettings @Inject constructor(

): ViewModel() {

    val userIntent = Channel<SettingsProfileIntent>(Channel.UNLIMITED)
    private var _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private var _oldPassword = MutableStateFlow("")
    val oldPassword: StateFlow<String> = _oldPassword

    private var _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private var _visibility = MutableStateFlow("")
    val visibility: StateFlow<String> = _visibility

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

    fun isPasswordMoreThanSixSymbols() = _oldPassword.value.length >= 6 && _newPassword.value.length >= 6
}