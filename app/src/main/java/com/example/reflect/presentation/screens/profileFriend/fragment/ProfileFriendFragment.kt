package com.example.reflect.presentation.screens.profileFriend.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentProfileFriendBinding
import com.example.reflect.domain.model.GetUserByIdModel
import com.example.reflect.presentation.screens.profileFriend.viewmodel.ViewModelUserProfile
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFriendFragment : Fragment() {

    private var _binding: FragmentProfileFriendBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelUserProfile by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileFriendBinding.inflate(inflater, container, false)

        val user: GetUserByIdModel = arguments?.getParcelable("userModel") ?: run {
            throw IllegalArgumentException("RecordModel is null")
        }

        user.username.let { vm.updateUsername(it) }
        user.friendshipStatus.let { vm.updateFriendship(it) }
        user.lastState?.let { vm.updateRecord(it) }
        user.week?.let { vm.updateLineChart(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLineChartProperties(requireContext())

        with (binding) {
            fragmentProfileUserLogin.text = vm.username.value
            registrationBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            fragmentProfileUserAddFriendButton.setOnClickListener {
                it.visibility = View.GONE
                fragmentProfileUserAddFriendButtonSendRequest.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            vm.lineChartState.collect {
                handleLineChartState(it)
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
//                            valueFormatter = LineChartXAxisFormatter(state.data.map { it.data.toString() }, TimeRange.WEEK)
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

}