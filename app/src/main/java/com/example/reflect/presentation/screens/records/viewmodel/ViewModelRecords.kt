package com.example.reflect.presentation.screens.records.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.usecase.state.DeleteStateUseCase
import com.example.reflect.domain.usecase.state.GetStatesUseCase
import com.example.reflect.domain.usecase.statistic.GetWeeklyAverageUseCase
import com.example.reflect.presentation.common.DateUtils
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
    val currentCalendar = Calendar.getInstance()
    val mutableCalendar = Calendar.getInstance()

    val userIntent = Channel<DeleteStateIntent>(Channel.UNLIMITED)

    private var _recordsState = MutableStateFlow<GetRecordsState>(GetRecordsState.Idle)
    val recordsState: StateFlow<GetRecordsState> = _recordsState

    private var _deleteState = MutableStateFlow<RecordState>(RecordState.Idle)
    val deleteState: StateFlow<RecordState> = _deleteState

    private var _selectedDate = MutableStateFlow(mutableCalendar.time)
    val selectedDate: StateFlow<Date> get() = _selectedDate

    private var _records = MutableStateFlow(emptyList<RecordModel>())
    val records: StateFlow<List<RecordModel>> get() = _records

    private var _selectedDateText = MutableStateFlow(DateUtils.dateToString(today = currentCalendar, currentDate = currentCalendar))
    val selectedDateText: StateFlow<String> get() = _selectedDateText

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

    fun updateSelectedDate(year: Int, month: Int, day: Int) {
        mutableCalendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        _selectedDate.value = mutableCalendar.time
        _selectedDateText.value = DateUtils.dateToString(today = currentCalendar, currentDate = mutableCalendar)
        fetchRecords()
    }

    fun updateSelectedDate() {
        _selectedDate.value = mutableCalendar.time
        Log.d("Ok", "Сейча ${currentCalendar.get(Calendar.DAY_OF_MONTH)}, а выбранно ${mutableCalendar.get(Calendar.DAY_OF_MONTH)}")
        _selectedDateText.value = DateUtils.dateToString(today = currentCalendar, currentDate = mutableCalendar)
        fetchRecords()
    }

    private suspend fun deleteRecord(id: Int) {
        _deleteState.value = RecordState.Idle
        deleteStateUseCase(id).collect { newState ->
            _deleteState.value = newState
        }
    }
}