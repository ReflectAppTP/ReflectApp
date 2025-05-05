package com.example.reflect.presentation.screens.addState.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.TagModel
import com.example.reflect.domain.usecase.GetFirstTagsUseCase
import com.example.reflect.presentation.screens.addState.AddStateIntent
import com.example.reflect.presentation.screens.addState.TagsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAddState @Inject constructor(
    private val getFirstTagsUseCase: GetFirstTagsUseCase
) : ViewModel() {

    val userIntent = Channel<AddStateIntent>(Channel.UNLIMITED)
    private val _firstTagsState = MutableStateFlow<TagsState>(TagsState.Idle)
    val firstTagsState: StateFlow<TagsState> = _firstTagsState

    private val _secondTagsState = MutableStateFlow<TagsState>(TagsState.Idle)
    val secondTagsState: StateFlow<TagsState> = _secondTagsState

    private var _emotionalState = MutableStateFlow(5f)
    val emotionalState: StateFlow<Float> get() = _emotionalState

    private var _firstTags = MutableStateFlow<List<TagModel>>(mutableListOf())
    val firstTags: StateFlow<List<TagModel>> get() = _firstTags

    private var _secondTags = MutableStateFlow<MutableList<TagModel>>(mutableListOf())
    val secondTags: StateFlow<MutableList<TagModel>> get() = _secondTags

    private var _emotionalDescription = MutableStateFlow("")
    val emotionalDescription: StateFlow<String> get() = _emotionalDescription

    private var _selectedFirstTags = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val selectedFirstTags: StateFlow<MutableList<Int>> get() = _selectedFirstTags

    private var _selectedSecondTags = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val selectedSecondTags: StateFlow<MutableList<Int>> get() = _selectedSecondTags

    init {
        fetchFirstTags()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AddStateIntent.AddState -> addState()
                }
            }
        }
    }

    private suspend fun addState() {
        // TODO:
    }

    private fun fetchFirstTags() {
        _firstTagsState.value = TagsState.Idle
        viewModelScope.launch {
            getFirstTagsUseCase().collect {
                _firstTagsState.value = it
                if (it is TagsState.Success) {
                    _firstTags.value = it.tags
                }
            }
        }
    }

    fun fetchSecondTags() {
        // TODO: impl
        _secondTags.value = mutableListOf(
            TagModel(1, "Удивленно", "\uD83D\uDE2E"),
            TagModel(2, "Спокойно", "\uD83D\uDE0C"),
            TagModel(3, "Удовлетворенно", "\uD83D\uDE0A"),
            TagModel(4, "Счастливо", "\uD83D\uDE01"),
            TagModel(5, "Расслабленно", "\uD83D\uDE34"),
            TagModel(6, "Безмятежно", "\uD83D\uDE07"),
            TagModel(7, "Окрыленно", "\uD83E\uDD29"),
            TagModel(8, "Воодушевленно", "\uD83D\uDE0D"),
            TagModel(9, "Устало", "\uD83D\uDE13"),
            TagModel(10, "Грустно", "\uD83D\uDE22"),
            TagModel(11, "Напряженно", "\uD83D\uDE15"),
            TagModel(12, "Депрессивно", "\uD83D\uDE2D"),
            TagModel(13, "В стрессе", "\uD83E\uDD2C"),
            TagModel(14, "Нервно", "\uD83E\uDD75"),
            TagModel(15, "Расстроенно", "\uD83D\uDE1E"),
            TagModel(16, "Скучно", "\uD83D\uDE14"),
            TagModel(17, "Тревожно", "\uD83E\uDD2F"),
            TagModel(18, "Отвратительно", "\uD83D\uDE21")
        )
    }

    fun updateEmotionalState(state: Float) {
        _emotionalState.value = state
    }

    fun updateEmotionalDescription(description: String) {
        _emotionalDescription.value = description
    }

    fun addTagIdToFirstList(id: Int) {
        _selectedFirstTags.value.add(id)
    }

    fun addTagIdToSecondList(id: Int) {
        _selectedSecondTags.value.add(id)
    }

    fun clearData() {
        _emotionalState.value = 5f
        _firstTags.value = mutableListOf()
        _secondTags.value = mutableListOf()
        _emotionalDescription.value = ""

        _selectedFirstTags.value = mutableListOf()
        _selectedSecondTags.value = mutableListOf()
    }
}