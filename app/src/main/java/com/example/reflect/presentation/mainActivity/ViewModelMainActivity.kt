package com.example.reflect.presentation.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.LoginModel
import com.example.reflect.domain.usecase.GetProfileUseCase
import com.example.reflect.domain.usecase.RefreshUseCase
import com.example.reflect.presentation.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMainActivity @Inject constructor(
    private val refreshUseCase: RefreshUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<GetProfileState>(GetProfileState.Idle)
    val state: StateFlow<GetProfileState> = _state

    fun validateUser(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            verifyAccessToken(accessToken, refreshToken)
        }
    }

    private suspend fun verifyAccessToken(accessToken: String, refreshToken: String) {
        getProfileUseCase(accessToken).collect {
            when(it) {
                is LoginState.SuccessGetProfile -> {
                    _state.value = GetProfileState.Success(LoginModel(refreshToken, accessToken))
                }
                is LoginState.Error -> {
                    if (it.code == 401) {
                        refreshTokens(refreshToken)
                    } else {
                        _state.value = GetProfileState.Error(it.message, it.code)
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun refreshTokens(refreshToken: String) {
        refreshUseCase(refreshToken).collect {
            _state.value = it
        }
    }
}