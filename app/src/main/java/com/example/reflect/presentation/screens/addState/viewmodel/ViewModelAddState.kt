package com.example.reflect.presentation.screens.addState.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.TagModel
import com.example.reflect.domain.usecase.AddStateUseCase
import com.example.reflect.domain.usecase.EditStateUseCase
import com.example.reflect.domain.usecase.GetFirstTagsUseCase
import com.example.reflect.domain.usecase.GetSecondTagsUseCase
import com.example.reflect.presentation.screens.addState.AddStateIntent
import com.example.reflect.presentation.screens.addState.RecordState
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
    private val getFirstTagsUseCase: GetFirstTagsUseCase,
    private val getSecondTagsUseCase: GetSecondTagsUseCase,
    private val addStateUseCase: AddStateUseCase,
    private val editStateUseCase: EditStateUseCase
) : ViewModel() {

    val userIntent = Channel<AddStateIntent>(Channel.UNLIMITED)
    private val _recordState = MutableStateFlow<RecordState>(RecordState.Idle)
    val recordState: StateFlow<RecordState> = _recordState

    private val _firstTagsState = MutableStateFlow<TagsState>(TagsState.Idle)
    val firstTagsState: StateFlow<TagsState> = _firstTagsState

    private val _secondTagsState = MutableStateFlow<TagsState>(TagsState.Idle)
    val secondTagsState: StateFlow<TagsState> = _secondTagsState

    private var _emotionalState = MutableStateFlow(5f)
    val emotionalState: StateFlow<Float> get() = _emotionalState

    private var _firstTags = MutableStateFlow<List<TagModel>>(mutableListOf())
    val firstTags: StateFlow<List<TagModel>> get() = _firstTags

    private var _secondTags = MutableStateFlow<List<TagModel>>(mutableListOf())
    val secondTags: StateFlow<List<TagModel>> get() = _secondTags

    private var _emotionalDescription = MutableStateFlow("")
    val emotionalDescription: StateFlow<String> get() = _emotionalDescription

    private var _selectedFirstTags = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val selectedFirstTags: StateFlow<MutableList<Int>> get() = _selectedFirstTags

    private var _selectedSecondTags = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val selectedSecondTags: StateFlow<MutableList<Int>> get() = _selectedSecondTags

    private var _id = MutableStateFlow<Int?>(null)
    val id: StateFlow<Int?> get() = _id

    init {
        fetchFirstTags()
        fetchSecondTags()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AddStateIntent.AddState -> addState()
                    is AddStateIntent.EditState -> editState()
                }
            }
        }
    }

    private suspend fun addState() {
        _recordState.value = RecordState.Idle
        addStateUseCase(
            _emotionalState.value.toInt(),
            _emotionalDescription.value,
            _selectedFirstTags.value,
            _selectedSecondTags.value
        ).collect { newState ->
            _recordState.value = newState
        }
    }

    private suspend fun editState() {
        _recordState.value = RecordState.Idle
        editStateUseCase(
            _id.value!!,
            _emotionalState.value.toInt(),
            _emotionalDescription.value,
            _selectedFirstTags.value,
            _selectedSecondTags.value
        ).collect { newState ->
            _recordState.value = newState
        }
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

    private fun fetchSecondTags() {
        _secondTagsState.value = TagsState.Idle
        viewModelScope.launch {
            getSecondTagsUseCase().collect {
                _secondTagsState.value = it
                if (it is TagsState.Success) {
                    _secondTags.value = it.tags
                }
            }
        }
    }

    fun updateEmotionalState(state: Float) {
        _emotionalState.value = state
    }

    fun updateEmotionalDescription(description: String) {
        _emotionalDescription.value = description
    }

    fun updateFirstTagIdsList(ids: MutableList<Int>) {
        _selectedFirstTags.value = ids
    }

    fun updateSecondTagIdsList(ids: MutableList<Int>) {
        _selectedSecondTags.value = ids
    }

    fun updateId(id: Int?) {
        _id.value = id
    }

    fun addTagIdToFirstList(id: Int) {
        _selectedFirstTags.value.add(id)
    }

    fun addTagIdToSecondList(id: Int) {
        _selectedSecondTags.value.add(id)
    }

    fun clearData() {
        _recordState.value = RecordState.Idle
        _firstTagsState.value = TagsState.Idle
        _secondTagsState.value = TagsState.Idle

        _emotionalState.value = 5f
//        _firstTags.value = mutableListOf()
//        _secondTags.value = mutableListOf()
        _emotionalDescription.value = ""
        _id.value = null

        _selectedFirstTags.value = mutableListOf()
        _selectedSecondTags.value = mutableListOf()
    }
}