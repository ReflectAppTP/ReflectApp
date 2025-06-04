package com.example.reflect.presentation.screens.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.auth.RegistrationFromGuestUseCase
import com.example.reflect.domain.usecase.auth.RegistrationUseCase
import com.example.reflect.presentation.screens.registration.RegistrationIntent
import com.example.reflect.presentation.screens.registration.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegistration @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val registrationFromGuestUseCase: RegistrationFromGuestUseCase,
) : ViewModel() {

    val userIntent = Channel<RegistrationIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val state: StateFlow<RegistrationState> = _state

    // TODO: remove unused values
    private val _login = MutableStateFlow("")
    val login: StateFlow<String> get() = _login

    private var _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _passwordConfirmation = MutableStateFlow("")
    val passwordConfirmation: StateFlow<String> get() = _passwordConfirmation

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when (it) {
                    is RegistrationIntent.RegisterUser -> register()
                    is RegistrationIntent.RegisterGuest -> registerGuest()
                }
            }
        }
    }

    private fun register() {
        _state.value = RegistrationState.Idle
        viewModelScope.launch {
            registrationUseCase(_login.value, _email.value, _password.value).collect { newState ->
                _state.value = newState
            }
        }
    }

    private fun registerGuest() {
        _state.value = RegistrationState.Idle
        viewModelScope.launch {
            registrationFromGuestUseCase(_login.value, _email.value, _password.value).collect { newState ->
                _state.value = newState
            }
        }
    }

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

    fun isPasswordMoreThanSixSymbols() = _password.value.length >= 6 && _passwordConfirmation.value.length >= 6
}