package com.example.reflect.presentation.screens.profileFriend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflect.common.FriendshipEnum
import com.example.reflect.common.UserVisibilityEnum
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.model.StatisticAverageModel
import com.example.reflect.domain.usecase.friendship.SendFriendshipRequestUseCase
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.screens.friends.SendFriendshipRequestState
import com.example.reflect.presentation.screens.profileFriend.ProfileUserIntent
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appmetrica.analytics.impl.id
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelUserProfile @Inject constructor(
    private val sendFriendshipRequestUseCase: SendFriendshipRequestUseCase
): ViewModel() {

    val userIntent = Channel<ProfileUserIntent>(Channel.UNLIMITED)
    private var _sendFriendshipRequestState = MutableStateFlow<SendFriendshipRequestState>(SendFriendshipRequestState.Idle)
    val sendFriendshipRequestState: StateFlow<SendFriendshipRequestState> = _sendFriendshipRequestState

    private var _id = MutableStateFlow(-1)
    val id: StateFlow<Int> = _id

    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private var _friendship = MutableStateFlow(FriendshipEnum.User)
    val friendship: StateFlow<FriendshipEnum> = _friendship

    private var _recordModel = MutableStateFlow<RecordModel?>(null)
    val recordModel: StateFlow<RecordModel?> = _recordModel

    private var _lineChartState = MutableStateFlow<LineChartState>(LineChartState.Idle)
    val lineChartState: StateFlow<LineChartState> = _lineChartState

    private var _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private var _visibility = MutableStateFlow(UserVisibilityEnum.All)
    val visibility: StateFlow<UserVisibilityEnum> = _visibility

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ProfileUserIntent.FriendRequest -> sendFriendshipRequest()
                    is ProfileUserIntent.SendReport -> Unit
                }
            }
        }
    }

    private fun sendFriendshipRequest() {
        _sendFriendshipRequestState.value = SendFriendshipRequestState.Loading
        viewModelScope.launch {
            sendFriendshipRequestUseCase(_id.value).collect { newState ->
                _sendFriendshipRequestState.value = newState
            }
        }
    }

    fun updateId(id: Int) {
        _id.value = id
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updateFriendship(friendship: FriendshipEnum) {
        _friendship.value = friendship
    }

    fun updateRecord(record: RecordModel?) {
        _recordModel.value = record
    }

    fun updatePremium(isPremium: Boolean) {
        _isPremium.value = isPremium
    }

    fun updateVisibility(visibilityEnum: UserVisibilityEnum) {
        _visibility.value = visibilityEnum
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