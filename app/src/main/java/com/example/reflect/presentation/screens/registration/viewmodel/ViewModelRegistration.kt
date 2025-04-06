package com.example.reflect.presentation.screens.registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelRegistration : ViewModel() {
    private val _login = MutableLiveData("")
    val login: LiveData<String> get() = _login

    private var _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private var _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _passwordConfirmation = MutableLiveData("")
    val passwordConfirmation: LiveData<String> get() = _passwordConfirmation

    fun updateLogin(result: String) {
        _login.value = result
    }

    fun updateEmail(result: String) {
        _email.value = result
    }

    fun updatePassword(result: String) {
        _password.value = result
    }

    fun updatePasswordConfirmation(result: String) {
        _passwordConfirmation.value = result
    }
}