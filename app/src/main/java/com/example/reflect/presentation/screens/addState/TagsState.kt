package com.example.reflect.presentation.screens.addState

import com.example.reflect.domain.model.TagModel

sealed class TagsState {
    data object Idle: TagsState()
    data object Loading: TagsState()
    data class Success(val tags: List<TagModel>): TagsState()
    data class Error(val message: String): TagsState()
}