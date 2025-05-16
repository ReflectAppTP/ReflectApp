package com.example.reflect.presentation.screens.ai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.AIHelperTextModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelAI @Inject constructor() : ViewModel() {

    private var _inputTextValue = MutableStateFlow("")
    val inputTextValue: StateFlow<String> = _inputTextValue

    private var _helperTextList = MutableStateFlow<List<AIHelperTextModel>>(mutableListOf())
    val helperTextList: StateFlow<List<AIHelperTextModel>> = _helperTextList

    init {
        fetchHelperTextList()
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
}