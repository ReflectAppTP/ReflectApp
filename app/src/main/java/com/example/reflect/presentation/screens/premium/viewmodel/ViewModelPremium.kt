package com.example.reflect.presentation.screens.premium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.friendship.ChangePremiumUseCase
import com.example.reflect.presentation.screens.premium.UpdatePremiumIntent
import com.example.reflect.presentation.screens.premium.UpdatePremiumState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPremium @Inject constructor(
    private val changePremiumUseCase: ChangePremiumUseCase
) : ViewModel(){
    val userIntent = Channel<UpdatePremiumIntent>(Channel.UNLIMITED)
    private var _state = MutableStateFlow<UpdatePremiumState>(UpdatePremiumState.Idle)
    val state: StateFlow<UpdatePremiumState> = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is UpdatePremiumIntent.Update -> updatePremium(it.isPremium)
                }
            }
        }
    }

    private fun updatePremium(isPremium: Boolean) {
        _state.value = UpdatePremiumState.Idle
        viewModelScope.launch {
            changePremiumUseCase(isPremium).collect {
                _state.value = it
            }
        }
    }
}