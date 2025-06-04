package com.example.reflect.presentation.screens.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.auth.GetProfileUseCase
import com.example.reflect.domain.usecase.auth.LoginLikeGuestUseCase
import com.example.reflect.domain.usecase.auth.LoginUseCase
import com.example.reflect.presentation.screens.login.LoginIntent
import com.example.reflect.presentation.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val loginLikeGuestUseCase: LoginLikeGuestUseCase,
) : ViewModel() {

    val userIntent = Channel<LoginIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    private var _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private var _emailErrorState = MutableStateFlow(false)
    val emailErrorState: StateFlow<Boolean> get() = _emailErrorState

    private var _passwordErrorState = MutableStateFlow(false)
    val passwordErrorState: StateFlow<Boolean> get() = _passwordErrorState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.LoginUser -> login()
                    is LoginIntent.LoginLikeGuest -> loginLikeGuest()
                }
            }
        }
    }

    private suspend fun login() {
        _state.value = LoginState.Idle
        loginUseCase(_email.value, _password.value).collect { newState ->
            _state.value = newState
            if (newState is LoginState.SuccessLogin) {
                getProfileUseCase().collect { newGetProfileState ->
                    _state.value = newGetProfileState
                }
            }
        }
    }

    private suspend fun loginLikeGuest() {
        _state.value = LoginState.Idle
        loginLikeGuestUseCase().collect { newState ->
            _state.value = newState
        }
    }

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