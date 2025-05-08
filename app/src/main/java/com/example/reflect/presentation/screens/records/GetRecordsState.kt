package com.example.reflect.presentation.screens.records

import com.example.reflect.domain.model.RecordModel

sealed class GetRecordsState {
    data object Idle: GetRecordsState()
    data object Loading: GetRecordsState()
    data class Success(val records: List<RecordModel>): GetRecordsState()
    data class Error(val message: String): GetRecordsState()
}