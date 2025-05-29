package com.example.reflect.presentation.screens.profileFriend.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelUserProfile @Inject constructor(): ViewModel() {

    private var _lineChartState = MutableStateFlow<LineChartState>(LineChartState.Idle)
    val lineChartState: StateFlow<LineChartState> = _lineChartState

    init {
        changeLineChartTest()
    }

    private fun changeLineChartTest() {
        _lineChartState.value = LineChartState.Success(
            listOf(
                Entry(1f,2f, "19"),
                Entry(2f,5f, "20"),
                Entry(3f,10f, "21"),
                Entry(4f,9f, "22"),
                Entry(5f,3f, "23"),
                Entry(6f,1f, "24"),
                Entry(7f,8f, "25"),
            ),
            TimeRange.WEEK
        )
    }
}