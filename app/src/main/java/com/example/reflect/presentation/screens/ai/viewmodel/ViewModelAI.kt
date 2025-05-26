package com.example.reflect.presentation.screens.ai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.AIHelperTextModel
import com.example.reflect.domain.model.AIMessageModel
import com.example.reflect.domain.usecase.ai.GetAIMessageUseCase
import com.example.reflect.domain.usecase.ai.ResetAIContextUseCase
import com.example.reflect.domain.usecase.ai.SendAIMessageUseCase
import com.example.reflect.presentation.screens.ai.AiIntent
import com.example.reflect.presentation.screens.ai.GetAIMessageState
import com.example.reflect.presentation.screens.ai.ResetAIContextState
import com.example.reflect.presentation.screens.ai.SendAIMessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAI @Inject constructor(
    private val sendAIMessageUseCase: SendAIMessageUseCase,
    private val resetAIContextUseCase: ResetAIContextUseCase,
    private val getAIMessageUseCase: GetAIMessageUseCase,
) : ViewModel() {

    val userIntent = Channel<AiIntent>(Channel.UNLIMITED)
    private val _sendMessageState = MutableStateFlow<SendAIMessageState>(SendAIMessageState.Idle)
    val sendMessageState: StateFlow<SendAIMessageState> = _sendMessageState
    private val _getMessageState = MutableStateFlow<GetAIMessageState>(GetAIMessageState.Idle)
    val getAIMessageState: StateFlow<GetAIMessageState> = _getMessageState
    private val _resetContextState = MutableStateFlow<ResetAIContextState>(ResetAIContextState.Idle)
    val resetContextState: StateFlow<ResetAIContextState> = _resetContextState

    private var _inputTextValue = MutableStateFlow("")
    val inputTextValue: StateFlow<String> = _inputTextValue

    private var _messagesList = MutableStateFlow<MutableList<AIMessageModel>>(mutableListOf())
    val messagesList: StateFlow<MutableList<AIMessageModel>> = _messagesList

    private var _helperTextList = MutableStateFlow<List<AIHelperTextModel>>(mutableListOf())
    val helperTextList: StateFlow<List<AIHelperTextModel>> = _helperTextList

    init {
        handleIntent()

        fetchHelperTextList()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AiIntent.SendAiMessage -> postAiMessage()
                    is AiIntent.ResetAiContext -> resetContext()
                }
            }
        }
    }

    private fun fetchHelperTextList() {
        _helperTextList.value = mutableListOf(
            AIHelperTextModel("Дай совет"),
            AIHelperTextModel("Проанализируй тренды"),
            AIHelperTextModel("Дай статистику"),
            AIHelperTextModel("Как улучшить мой состояние"),

        )
    }

    private suspend fun postAiMessage() {
        _sendMessageState.value = SendAIMessageState.Idle
        sendAIMessageUseCase(_inputTextValue.value).collect { newState ->
            _sendMessageState.value = newState
            if (newState is SendAIMessageState.Success) {
                _getMessageState.value = GetAIMessageState.Loading
                var successState = true
                while (successState) {
                    getAIMessageUseCase(newState.message.messageId + 1).collect {
                        if (it is GetAIMessageState.Success && it.messageModel.status == "pending") {
                            if (_getMessageState.value !is GetAIMessageState.Loading) {
                                _getMessageState.value = GetAIMessageState.Loading
                            }
                            delay(4000)
                        } else {
                            if (it is GetAIMessageState.Success) {
                                _messagesList.value.add(
                                    AIMessageModel(it.messageModel.response!!, true)
                                )
                                successState = false
                            }
                            _getMessageState.value = it
                            Log.d("OkHTTP", if (_getMessageState.value is GetAIMessageState.Error) (_getMessageState.value as GetAIMessageState.Error).message else "ecas")
                        }
                    }
                }
            }
        }
    }

    fun updateText(result: String) {
        _inputTextValue.value = result
    }

    fun updateTextWithHelper(helperText: String) {
        _inputTextValue.value += "$helperText "
    }

    fun addMessage() {
        _messagesList.value.add(AIMessageModel(_inputTextValue.value, false))
    }

    private suspend fun resetContext() {
        _resetContextState.value = ResetAIContextState.Idle
        resetAIContextUseCase().collect { newState ->
            if (newState is ResetAIContextState.Success) {
                _messagesList.value = mutableListOf()
            }
            _resetContextState.value = newState
        }
    }

    fun cleanStates() {
        _resetContextState.value = ResetAIContextState.Idle
//        _sendMessageState.value = SendAIMessageState.Idle
//        _getMessageState.value = GetAIMessageState.Idle
    }
}