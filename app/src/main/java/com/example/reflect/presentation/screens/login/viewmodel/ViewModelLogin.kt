package com.example.reflect.presentation.screens.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor() : ViewModel() {
    private var _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private var _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private var _emailErrorState = MutableLiveData(false)
    val emailErrorState: LiveData<Boolean> get() = _emailErrorState

    private var _passwordErrorState = MutableLiveData(false)
    val passwordErrorState: LiveData<Boolean> get() = _passwordErrorState

    fun updateEmail(result: String) {
        _email.value = result
    }

    fun updatePassword(result: String) {
        _password.value = result
    }

    fun changeErrorStates(emailError: Boolean = true, passwordError: Boolean = true) {
        _emailErrorState.value = emailError
        _passwordErrorState.value = passwordError
    }
}