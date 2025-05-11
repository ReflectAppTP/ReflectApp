package com.example.reflect.presentation.screens.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.statistic.GetFrequencyUseCase
import com.example.reflect.domain.usecase.statistic.GetStatisticEmotionalTagsUseCase
import com.example.reflect.domain.usecase.statistic.GetStatisticTagsUseCase
import com.example.reflect.presentation.common.DateUtils
import com.example.reflect.presentation.common.DateUtils.getStringFromDate
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.states.StatisticTagState
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.example.reflect.presentation.screens.statistics.states.PieChartState
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class VIewModelStatistic @Inject constructor(
    private val getFrequencyUseCase: GetFrequencyUseCase,
    private val getStatisticTagsUseCase: GetStatisticTagsUseCase,
    private val getStatisticEmotionalTagsUseCase: GetStatisticEmotionalTagsUseCase,
): ViewModel() {

    private val currentCalendar = Calendar.getInstance()
    private val todayCalendar = Calendar.getInstance()
    private val selectedCalendar = Calendar.getInstance()

    val userIntent = Channel<StatisticIntent>(Channel.UNLIMITED)

    private var _lineChartState = MutableStateFlow<LineChartState>(LineChartState.Idle)
    val lineChartState: StateFlow<LineChartState> = _lineChartState

    private var _pieChartState = MutableStateFlow<PieChartState>(PieChartState.Idle)
    val pieChartState: StateFlow<PieChartState> = _pieChartState

    private var _firstStatisticTagState = MutableStateFlow<StatisticTagState>(StatisticTagState.Idle)
    val firstStatisticTagState: StateFlow<StatisticTagState> = _firstStatisticTagState

    private var _secondStatisticTagState = MutableStateFlow<StatisticTagState>(StatisticTagState.Idle)
    val secondStatisticTagState: StateFlow<StatisticTagState> = _secondStatisticTagState


    private var _timeRangeTitle = MutableStateFlow("")
    val timeRangeTitle: StateFlow<String> = _timeRangeTitle

    init {
        getWeekStatistic()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is StatisticIntent.WeekStatistic -> getWeekStatistic()
                    is StatisticIntent.MonthStatistic -> getMonthStatistic()
                    is StatisticIntent.YearStatistic -> getYearStatistic()
                }
            }
        }
    }

    private fun getWeekStatistic() {
        selectedCalendar.time = currentCalendar.time
        todayCalendar.time = currentCalendar.time
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        val currentDate = todayCalendar.time
        selectedCalendar.add(Calendar.DAY_OF_MONTH, -7)
        val selectedDate = selectedCalendar.time

        val startDate = getStringFromDate(selectedDate)
        val endDate = getStringFromDate(currentDate)

        _timeRangeTitle.value = DateUtils.getWeekRange(currentCalendar, selectedCalendar)

        _lineChartState.value = LineChartState.Loading

        _pieChartState.value = PieChartState.Idle
        viewModelScope.launch {
            getFrequencyUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _pieChartState.value = newState
            }
        }


        _firstStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticEmotionalTagsUseCase(startDate = startDate, endDate = endDate)
                .collect{ newState ->
                    _firstStatisticTagState.value = newState
                }
        }

        _secondStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticTagsUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _secondStatisticTagState.value = newState
                }
        }
    }

    private fun getMonthStatistic() {
        selectedCalendar.time = currentCalendar.time
        todayCalendar.time = currentCalendar.time
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        val currentDate = todayCalendar.time
        selectedCalendar.add(Calendar.MONTH, -1)
        val selectedDate = selectedCalendar.time

        val startDate = getStringFromDate(selectedDate)
        val endDate = getStringFromDate(currentDate)

        _timeRangeTitle.value = DateUtils.getMonthRange(currentCalendar, selectedCalendar)
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(listOf(
            Entry(0f, 1f),
            Entry(2f, 10f),
            Entry(5f, 4f),
            Entry(6f, 2f),
            Entry(7f, 1f),
            Entry(8f, 7f),
            Entry(9f, 8f),
            Entry(12f, 10f),
            Entry(15f, 3f),
            Entry(16f, 10f),
            Entry(17f, 10f),
            Entry(18f, 7f),
            Entry(20f, 1f),
            Entry(21f, 10f),
            Entry(24f, 6f),
        ))

        _pieChartState.value = PieChartState.Idle
        viewModelScope.launch {
            getFrequencyUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _pieChartState.value = newState
                }
        }

        _firstStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticEmotionalTagsUseCase(startDate = startDate, endDate = endDate)
                .collect{ newState ->
                    _firstStatisticTagState.value = newState
                }
        }

        _secondStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticTagsUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _secondStatisticTagState.value = newState
                }
        }
    }

    private fun getYearStatistic() {
        selectedCalendar.time = currentCalendar.time
        todayCalendar.time = currentCalendar.time
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        val currentDate = todayCalendar.time
        selectedCalendar.add(Calendar.YEAR, -1)
        val selectedDate = selectedCalendar.time

        val startDate = getStringFromDate(selectedDate)
        val endDate = getStringFromDate(currentDate)

        _timeRangeTitle.value = DateUtils.getYearRange(currentCalendar, selectedCalendar)
        // TODO: Запрос
        _lineChartState.value = LineChartState.Loading
        _lineChartState.value = LineChartState.Success(mutableListOf())

        _pieChartState.value = PieChartState.Idle
        viewModelScope.launch {
            getFrequencyUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _pieChartState.value = newState
                }
        }

        _firstStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticEmotionalTagsUseCase(startDate = startDate, endDate = endDate)
                .collect{ newState ->
                    _firstStatisticTagState.value = newState
                }
        }

        _secondStatisticTagState.value = StatisticTagState.Idle
        viewModelScope.launch {
            getStatisticTagsUseCase(startDate = startDate, endDate = endDate)
                .collect { newState ->
                    _secondStatisticTagState.value = newState
                }
        }
    }

}