package com.example.reflect.presentation.screens.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.GetProfileUseCase
import com.example.reflect.domain.usecase.LoginUseCase
import com.example.reflect.presentation.screens.login.LoginIntent
import com.example.reflect.presentation.screens.login.LoginState
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
class ViewModelLogin @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase
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
                }
            }
        }
    }

    private fun login() {
        _state.value = LoginState.Idle
        viewModelScope.launch {
            loginUseCase(_email.value, _password.value)
                .onEach { newState ->
                    _state.value = newState
                    if (newState is LoginState.SuccessLogin) {
                        getProfileUseCase()
                            .onEach { newGetProfileState ->
                                _state.value = newGetProfileState
                            }
                            .launchIn(viewModelScope)
                    }
                }
                .launchIn(viewModelScope)
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