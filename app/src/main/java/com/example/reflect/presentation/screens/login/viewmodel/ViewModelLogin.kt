package com.example.reflect.presentation.screens.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelLogin : ViewModel() {
    private var _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private var _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    fun updateEmail(result: String) {
        _email.value = result
    }

    fun updatePassword(result: String) {
        _password.value = result
    }
}