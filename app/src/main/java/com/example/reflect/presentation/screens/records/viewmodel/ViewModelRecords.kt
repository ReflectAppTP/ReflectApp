package com.example.reflect.presentation.screens.records.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.usecase.state.DeleteStateUseCase
import com.example.reflect.domain.usecase.state.GetStatesUseCase
import com.example.reflect.domain.usecase.state.GetStreakUseCase
import com.example.reflect.presentation.common.DateUtils
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.records.DeleteStateIntent
import com.example.reflect.presentation.screens.records.GetRecordsState
import com.example.reflect.presentation.screens.records.GetStreakState
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
    private val deleteStateUseCase: DeleteStateUseCase,
    private val getStreakUseCase: GetStreakUseCase,
) : ViewModel() {
    // Надо это поле делать private или нет?
    val currentCalendar = Calendar.getInstance()
    val mutableCalendar = Calendar.getInstance()

    val userIntent = Channel<DeleteStateIntent>(Channel.UNLIMITED)

    private var _recordsState = MutableStateFlow<GetRecordsState>(GetRecordsState.EmptyContent)
    val recordsState: StateFlow<GetRecordsState> = _recordsState

    private var _deleteState = MutableStateFlow<RecordState>(RecordState.Idle)
    val deleteState: StateFlow<RecordState> = _deleteState

    private var _streakState = MutableStateFlow<GetStreakState>(GetStreakState.Idle)
    val streakState: StateFlow<GetStreakState> = _streakState

    private var _selectedDate = MutableStateFlow(mutableCalendar.time)
    val selectedDate: StateFlow<Date> get() = _selectedDate

    private var _records = MutableStateFlow(emptyList<RecordModel>())
    val records: StateFlow<List<RecordModel>> get() = _records

    private var _selectedDateText = MutableStateFlow(DateUtils.dateToString(today = currentCalendar, currentDate = currentCalendar))
    val selectedDateText: StateFlow<String> get() = _selectedDateText

    init {
        fetchRecords()
        updateStreak()

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
                    updateStreak()
                }
                _recordsState.value = newState
            }
        }
    }

    private fun updateStreak() {
        _streakState.value = GetStreakState.Idle
        viewModelScope.launch {
            getStreakUseCase().collect { streakState ->
                _streakState.value = streakState
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
        _selectedDateText.value = DateUtils.dateToString(today = currentCalendar, currentDate = mutableCalendar)
        fetchRecords()
    }

    private suspend fun deleteRecord(id: Int) {
        _deleteState.value = RecordState.Idle
        deleteStateUseCase(id).collect { newState ->
            if (newState is RecordState.Success) {
                updateStreak()
            }
            _deleteState.value = newState
        }
    }

    fun datesAreEquals(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = _selectedDate.value
        return calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
            calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
    }
}