package com.example.reflect.presentation.screens.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.domain.usecase.statistic.GetFrequencyUseCase
import com.example.reflect.domain.usecase.statistic.GetMonthlyAverageUseCase
import com.example.reflect.domain.usecase.statistic.GetStatisticEmotionalTagsUseCase
import com.example.reflect.domain.usecase.statistic.GetStatisticTagsUseCase
import com.example.reflect.domain.usecase.statistic.GetWeeklyAverageUseCase
import com.example.reflect.domain.usecase.statistic.GetYearlyAverageUseCase
import com.example.reflect.presentation.common.DateUtils
import com.example.reflect.presentation.common.DateUtils.getStringFromDate
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.states.StatisticTagState
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.example.reflect.presentation.screens.statistics.states.PieChartState
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
    private val getWeeklyAverageUseCase: GetWeeklyAverageUseCase,
    private val getMonthlyAverageUseCase: GetMonthlyAverageUseCase,
    private val getYearlyAverageUseCase: GetYearlyAverageUseCase,
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

    private var _time = MutableStateFlow(TimeRange.WEEK)
    val time: StateFlow<TimeRange> = _time

    init {
        getWeekStatistic()

        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is StatisticIntent.WeekStatistic -> {
                        getWeekStatistic()
                        _time.value = TimeRange.WEEK
                    }
                    is StatisticIntent.MonthStatistic -> {
                        getMonthStatistic()
                        _time.value = TimeRange.MONTH
                    }
                    is StatisticIntent.YearStatistic -> {
                        getYearStatistic()
                        _time.value = TimeRange.YEAR
                    }
                    is StatisticIntent.UpdateStatistic -> {
                        when (_time.value) {
                            TimeRange.WEEK -> getWeekStatistic()
                            TimeRange.MONTH -> getMonthStatistic()
                            TimeRange.YEAR -> getYearStatistic()
                        }
                    }
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

        _lineChartState.value = LineChartState.Idle
        viewModelScope.launch {
            getWeeklyAverageUseCase().collect { newState ->
                _lineChartState.value = newState
            }
        }

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

        _lineChartState.value = LineChartState.Idle
        viewModelScope.launch {
            getMonthlyAverageUseCase().collect { newState ->
                _lineChartState.value = newState
            }
        }

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
        selectedCalendar.roll(Calendar.YEAR, -1)
//        selectedCalendar.add(Calendar.YEAR, -1)
        val selectedDate = selectedCalendar.time

        val startDate = getStringFromDate(selectedDate)
        val endDate = getStringFromDate(currentDate)

        _timeRangeTitle.value = DateUtils.getYearRange(currentCalendar, selectedCalendar)

        _lineChartState.value = LineChartState.Idle
        viewModelScope.launch {
            getYearlyAverageUseCase().collect { newState ->
                _lineChartState.value = newState
            }
        }

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