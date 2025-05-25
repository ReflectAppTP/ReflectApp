package com.example.reflect.presentation.screens.ai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.AIHelperTextModel
import com.example.reflect.domain.model.AIMessageModel
import com.example.reflect.domain.usecase.ai.SendAIMessageUseCase
import com.example.reflect.presentation.screens.ai.AiIntent
import com.example.reflect.presentation.screens.ai.SendAIMessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAI @Inject constructor(
    private val sendAIMessageUseCase: SendAIMessageUseCase
) : ViewModel() {

    val userIntent = Channel<AiIntent>(Channel.UNLIMITED)
    private val _sendMessageState = MutableStateFlow<SendAIMessageState>(SendAIMessageState.Idle)

    private var _inputTextValue = MutableStateFlow("")
    val inputTextValue: StateFlow<String> = _inputTextValue

    private var _messagesList = MutableStateFlow<List<AIMessageModel>>(mutableListOf())
    val messagesList: StateFlow<List<AIMessageModel>> = _messagesList

    private var _helperTextList = MutableStateFlow<List<AIHelperTextModel>>(mutableListOf())
    val helperTextList: StateFlow<List<AIHelperTextModel>> = _helperTextList

    init {
        handleIntent()

        fetchHelperTextList()
        fetchMessages()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AiIntent.SendAiMessage -> postAiMessage()
                }
            }
        }
    }

    private fun fetchMessages() {
        _messagesList.value = mutableListOf(
            AIMessageModel("Тут запрос", false),
            AIMessageModel("Тут ответ", true),
            AIMessageModel("Тут запрос снова", false),
            AIMessageModel("А тут запрос конкретный, вдруг интернет глюкнет и всё, грустить будем сильно мяу мяу", false),
            AIMessageModel("Тут ответ делюкс", true),
            AIMessageModel("Тут запрос 2", false),
            AIMessageModel("Тут ответ 2", true),
            AIMessageModel("Тут запрос снова 2", false),
            AIMessageModel("А тут запрос конкретный, вдруг интернет глюкнет и всё, грустить будем сильно мяу мяу 2", false),
            AIMessageModel("Тут ответ делюкс 2", true),
        )
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
                _inputTextValue.value = "ГОЙДАААА"
            }
        }
    }

    fun updateText(result: String) {
        _inputTextValue.value = result
    }

    fun updateTextWithHelper(helperText: String) {
        _inputTextValue.value += "$helperText "
    }

    fun cleanMessages() {
        _messagesList.value = emptyList()
    }
}