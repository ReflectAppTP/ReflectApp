package com.example.reflect.presentation.screens.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.RegistrationUseCase
import com.example.reflect.presentation.screens.registration.RegistrationIntent
import com.example.reflect.presentation.screens.registration.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegistration @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
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

    private var _loginErrorState = MutableStateFlow(false)
    val loginErrorState: StateFlow<Boolean> get() = _loginErrorState

    private var _emailErrorState = MutableStateFlow(false)
    val emailErrorState: StateFlow<Boolean> get() = _emailErrorState

    private var _passwordErrorState = MutableStateFlow(false)
    val passwordErrorState: StateFlow<Boolean> get() = _passwordErrorState

    private var _passwordConfirmationErrorState = MutableStateFlow(false)
    val passwordConfirmationErrorState: StateFlow<Boolean> get() = _passwordConfirmationErrorState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when (it) {
                    is RegistrationIntent.RegisterUser -> register()
                }
            }
        }
    }

    private fun register() {
        _state.value = RegistrationState.Idle
        viewModelScope.launch {
            registrationUseCase(_login.value, _email.value, password.value).collect { newState ->
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