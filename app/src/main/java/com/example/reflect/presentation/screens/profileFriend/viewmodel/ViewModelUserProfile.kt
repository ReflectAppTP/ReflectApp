package com.example.reflect.presentation.screens.profileFriend.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.common.FriendshipEnum
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelUserProfile @Inject constructor(): ViewModel() {

    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private var _friendship = MutableStateFlow(FriendshipEnum.User)
    val friendship: StateFlow<FriendshipEnum> = _friendship

    private var _recordModel = MutableStateFlow<RecordModel?>(null)
    val recordModel: StateFlow<RecordModel?> = _recordModel

    private var _lineChartState = MutableStateFlow<LineChartState>(LineChartState.Idle)
    val lineChartState: StateFlow<LineChartState> = _lineChartState

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updateFriendship(friendship: FriendshipEnum) {
        _friendship.value = friendship
    }

    fun updateRecord(record: RecordModel?) {
        _recordModel.value = record
    }

    fun updateLineChart(lineChart: List<StatisticAverageModel>?) {
        _lineChartState.value = if (lineChart == null) {
            LineChartState.Success(emptyList(), TimeRange.WEEK)
        } else {
            LineChartState.Success(lineChart.toEntries(), TimeRange.WEEK)
        }
    }

    private fun List<StatisticAverageModel>.toEntries(): List<Entry> {
        val sortedList = this.sortedBy { it.date }
        return sortedList.mapIndexed { index, model ->
            Entry(index.toFloat(), model.averageMood, model.date)
        }
    }
}