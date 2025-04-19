package com.example.reflect.presentation.screens.registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelRegistration @Inject constructor() : ViewModel() {
    // TODO: remove unused values 
    private val _login = MutableLiveData("")
    val login: LiveData<String> get() = _login

    private var _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private var _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _passwordConfirmation = MutableLiveData("")
    val passwordConfirmation: LiveData<String> get() = _passwordConfirmation

    private var _loginErrorState = MutableLiveData(false)
    val loginErrorState: LiveData<Boolean> get() = _loginErrorState

    private var _emailErrorState = MutableLiveData(false)
    val emailErrorState: LiveData<Boolean> get() = _emailErrorState

    private var _passwordErrorState = MutableLiveData(false)
    val passwordErrorState: LiveData<Boolean> get() = _passwordErrorState

    private var _passwordConfirmationErrorState = MutableLiveData(false)
    val passwordConfirmationErrorState: LiveData<Boolean> get() = _passwordConfirmationErrorState


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

    fun changeErrorStates(
        loginError: Boolean = true,
        emailError: Boolean = true,
        passwordError: Boolean = true,
        passwordConfirmationError: Boolean = true) {
        _loginErrorState.value = loginError
        _emailErrorState.value = emailError
        _passwordErrorState.value = passwordError
        _passwordConfirmationErrorState.value = passwordConfirmationError
    }

    fun isPasswordMoreThanSixSymbols() = _password.value!!.length >= 6 && _passwordConfirmation.value!!.length >= 6
}