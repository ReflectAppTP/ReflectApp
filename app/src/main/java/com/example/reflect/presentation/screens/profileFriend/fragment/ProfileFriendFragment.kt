package com.example.reflect.presentation.screens.profileFriend.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.common.FriendshipEnum
import com.example.reflect.common.UserVisibilityEnum
import com.example.reflect.databinding.FragmentProfileFriendBinding
import com.example.reflect.domain.model.GetUserByIdModel
import com.example.reflect.presentation.adapter.RecordsListAdapter
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.common.formatter.LineChartXAxisFormatter
import com.example.reflect.presentation.screens.friends.SendFriendshipRequestState
import com.example.reflect.presentation.screens.profileFriend.ProfileUserIntent
import com.example.reflect.presentation.screens.profileFriend.viewmodel.ViewModelUserProfile
import com.example.reflect.presentation.screens.records.GetRecordsState
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class ProfileFriendFragment : Fragment() {

    private var _binding: FragmentProfileFriendBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelUserProfile by viewModels()

    private lateinit var recordAdapter: RecordsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileFriendBinding.inflate(inflater, container, false)

        val user: GetUserByIdModel = arguments?.getParcelable("userModel") ?: run {
            throw IllegalArgumentException("RecordModel is null")
        }

        Log.d("User", user.toString())

        user.id.let { vm.updateId(it) }
        user.username.let { vm.updateUsername(it) }
        user.friendshipStatus.let { vm.updateFriendship(it) }
        user.isPremium.let { vm.updatePremium(it) }
        user.visibility.let { vm.updateVisibility(it) }
        vm.updateRecord(user.lastState)
        vm.updateLineChart(user.week)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLineChartProperties(requireContext())

        with (binding) {
            fragmentProfileUserLogin.text = vm.username.value
            fragmentProfileUserPremiumIcon.visibility = if (vm.isPremium.value) View.VISIBLE else View.GONE
            fragmentProfileUserBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            
            when (vm.friendship.value) {
                FriendshipEnum.User -> {
                    fragmentProfileUserAddFriendButton.visibility = View.VISIBLE
                    fragmentProfileUserAddFriendButtonFriend.visibility = View.GONE
                    fragmentProfileUserAddFriendButtonBanned.visibility = View.GONE
                }
                FriendshipEnum.Friend -> {
                    fragmentProfileUserAddFriendButton.visibility = View.GONE
                    fragmentProfileUserAddFriendButtonFriend.visibility = View.VISIBLE
                    fragmentProfileUserAddFriendButtonBanned.visibility = View.GONE
                }
                FriendshipEnum.Banned -> {
                    fragmentProfileUserAddFriendButton.visibility = View.GONE
                    fragmentProfileUserAddFriendButtonFriend.visibility = View.GONE
                    fragmentProfileUserAddFriendButtonBanned.visibility = View.VISIBLE
                }
            }
            fragmentProfileUserAddFriendButton.setOnClickListener {
                // TODO: add logic
                lifecycleScope.launch {
                    vm.userIntent.send(ProfileUserIntent.FriendRequest)
                }
            }

            if (vm.visibility.value == UserVisibilityEnum.Self ||
                (vm.visibility.value == UserVisibilityEnum.Friends && vm.friendship.value != FriendshipEnum.Friend)
                ) {
                fragmentProfileUserContentRootScrollView.visibility = View.GONE
                fragmentProfileUserContentHideTitle.visibility = View.VISIBLE
            }

            fragmentProfileUserContentLastRecordRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recordAdapter = RecordsListAdapter(Calendar.getInstance())
            if (vm.recordModel.value == null) {
                recordAdapter.updateState(GetRecordsState.EmptyContent)
            } else {
                recordAdapter.updateState(GetRecordsState.Success(listOf(vm.recordModel.value!!)))
            }
            fragmentProfileUserContentLastRecordRV.adapter = recordAdapter
        }

        lifecycleScope.launch {
            vm.lineChartState.collect {
                handleLineChartState(it)
            }
        }

        lifecycleScope.launch {
            vm.sendFriendshipRequestState.collect {
                handleSendFriendshipRequestState(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleLineChartState(state: LineChartState) {
        with (binding) {
            when (state) {
                is LineChartState.Loading -> {
                    fragmentProfileUserContentStatisticLottieLineChart.visibility = View.VISIBLE
                    fragmentProfileUserContentStatisticLineChart.visibility = View.GONE
                }
                is LineChartState.Success -> {
                    fragmentProfileUserContentStatisticLottieLineChart.visibility = View.GONE
                    fragmentProfileUserContentStatisticLineChart.visibility = View.VISIBLE

                    fragmentProfileUserContentStatisticLineChart.data = if (state.data.isEmpty()) null else LineData(
                        LineDataSet(state.data, " ").apply {
                            lineWidth = 5f
                            color = ContextCompat.getColor(requireContext(), R.color.tertiary)
                            circleColors = mutableListOf(ContextCompat.getColor(requireContext(), R.color.tertiary))
                            circleRadius = 5f
                            circleHoleRadius = 2f
                        }).apply {
                        fragmentProfileUserContentStatisticLineChart.xAxis.apply {
                            axisMinimum = xMin
                            axisMaximum = xMax
                            labelCount = state.data.size
                            granularity = 1f
                            valueFormatter = LineChartXAxisFormatter(state.data.map { it.data.toString() }, TimeRange.WEEK)
                        }
                        setDrawValues(false)
                    }
                    fragmentProfileUserContentStatisticLineChart.animateX(state.data.size * 80)
                }
                is LineChartState.Error -> {
                    fragmentProfileUserContentStatisticLottieLineChart.visibility = View.GONE
                    fragmentProfileUserContentStatisticLineChart.visibility = View.VISIBLE
                    fragmentProfileUserContentStatisticLineChart.data = null
                }
                is LineChartState.Idle -> {
                    Unit
                }
            }
        }
    }

    private fun setLineChartProperties(context: Context) {
        with (binding) {
            with (fragmentProfileUserContentStatisticLineChart) {
                setExtraOffsets(20f,20f,20f,20f)
                isDoubleTapToZoomEnabled = false
                setTouchEnabled(false)

                val testSize = 16f
                xAxis.apply {
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    textColor = ContextCompat.getColor(context, R.color.onSurface)
                    textSize = testSize
                    setDrawGridLines(true)
                    gridColor = ContextCompat.getColor(context, R.color.onSurface)
                }

                axisRight.apply {
                    setDrawTopYLabelEntry(false)
                    setDrawZeroLine(true)
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    gridColor = ContextCompat.getColor(context, R.color.onSurface)
                    axisMinimum = 0f
                    axisMaximum = 10f
                }

                axisLeft.apply {
                    setDrawTopYLabelEntry(false)
                    setDrawGridLines(true)
                    gridColor = ContextCompat.getColor(context, R.color.onSurface)
                    setDrawZeroLine(false)
                    setDrawLabels(false)
                    axisMinimum = 0f
                    axisMaximum = 10f
                    textColor = ContextCompat.getColor(context, R.color.onSurface)
                    textSize = testSize
                }

                legend.isEnabled = false
                description.isEnabled = false

                setNoDataText(context.resources.getString(R.string.fragmentStatisticEmptyChartData))
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 60f
                    color = ContextCompat.getColor(context, R.color.onSurface)
                }
                invalidate()
            }
        }
    }

    private fun handleSendFriendshipRequestState(state: SendFriendshipRequestState) {
        with (binding) {
            when (state) {
                is SendFriendshipRequestState.Loading -> {
                    fragmentProfileUserAddFriendButtonSendRequest.isEnabled = false
                }
                is SendFriendshipRequestState.Success -> {
                    fragmentProfileUserAddFriendButtonSendRequest.isEnabled = true
                    fragmentProfileUserAddFriendButton.visibility = View.GONE
                    fragmentProfileUserAddFriendButtonSendRequest.visibility = View.VISIBLE
                }
                is SendFriendshipRequestState.Error -> {
                    fragmentProfileUserAddFriendButtonSendRequest.isEnabled = true
                    fragmentProfileUserAddFriendButton.visibility = View.VISIBLE
                    fragmentProfileUserAddFriendButtonSendRequest.visibility = View.GONE
                    ToastUtils.showErrorToast(requireContext())
                }
                is SendFriendshipRequestState.Idle -> Unit
            }
        }
    }

}