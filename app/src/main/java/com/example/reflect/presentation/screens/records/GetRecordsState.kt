package com.example.reflect.presentation.screens.records

import com.example.reflect.domain.model.RecordModel

sealed class GetRecordsState(val viewType: Int) {
    data object EmptyContent: GetRecordsState(1)
    data object Loading: GetRecordsState(2)
    data class Success(val records: List<RecordModel>): GetRecordsState(3)
    data class Error(val message: String): GetRecordsState(4)
}