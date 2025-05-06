package com.example.reflect.presentation.screens.records.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.usecase.DeleteStateUseCase
import com.example.reflect.domain.usecase.GetStatesUseCase
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.records.DeleteStateIntent
import com.example.reflect.presentation.screens.records.GetRecordsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ViewModelRecords @Inject constructor(
    private val getStatesUseCase: GetStatesUseCase,
    private val deleteStateUseCase: DeleteStateUseCase
) : ViewModel() {
    // Надо это поле делать private или нет?
    val calendar = Calendar.getInstance()

    val userIntent = Channel<DeleteStateIntent>(Channel.UNLIMITED)

    private var _recordsState = MutableStateFlow<GetRecordsState>(GetRecordsState.Idle)
    val recordsState: StateFlow<GetRecordsState> = _recordsState

    private var _deleteState = MutableStateFlow<RecordState>(RecordState.Idle)
    val deleteState: StateFlow<RecordState> = _deleteState

    private var _selectedDate = MutableStateFlow(calendar.time)
    val selectedDate: StateFlow<Date> get() = _selectedDate

    private var _records = MutableStateFlow(emptyList<RecordModel>())
    val records: StateFlow<List<RecordModel>> get() = _records

    init {
        fetchRecords()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is DeleteStateIntent.DeleteRecord -> deleteRecord(it.id)
                }
            }
        }
    }

    fun fetchRecords(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        viewModelScope.launch {
            getStatesUseCase(dateFormat.format(_selectedDate.value)).collect { newState ->
                if (newState is GetRecordsState.Success) {
                    _records.value = newState.records.sortedBy { it.id }
                }
                _recordsState.value = newState
            }
        }
    }

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        fetchRecords()
    }

    private suspend fun deleteRecord(id: Int) {
        _deleteState.value = RecordState.Idle
        deleteStateUseCase(id).collect { newState ->
            _deleteState.value = newState
        }
    }
}