package com.example.reflect.presentation.screens.addState

import com.example.reflect.domain.model.RecordModel

sealed class RecordState {
    data object Idle: RecordState()
    data object Loading: RecordState()
    data class Success(val record: RecordModel): RecordState()
    data class Error(val message: String): RecordState()
}