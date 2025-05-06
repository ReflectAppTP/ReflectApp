package com.example.reflect.presentation.screens.records

sealed class DeleteStateIntent {
    data class DeleteRecord(val id: Int): DeleteStateIntent()
}