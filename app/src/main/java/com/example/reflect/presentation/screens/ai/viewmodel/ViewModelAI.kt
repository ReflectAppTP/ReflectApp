package com.example.reflect.presentation.screens.ai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.AIHelperTextModel
import com.example.reflect.domain.model.AIMessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelAI @Inject constructor() : ViewModel() {

    private var _inputTextValue = MutableStateFlow("")
    val inputTextValue: StateFlow<String> = _inputTextValue

    private var _messagesList = MutableStateFlow<List<AIMessageModel>>(mutableListOf())
    val messagesList: StateFlow<List<AIMessageModel>> = _messagesList

    private var _helperTextList = MutableStateFlow<List<AIHelperTextModel>>(mutableListOf())
    val helperTextList: StateFlow<List<AIHelperTextModel>> = _helperTextList

    init {
        fetchHelperTextList()
        fetchMessages()
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