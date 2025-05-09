package com.example.reflect.presentation.screens.statistics.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.reflect.R
import com.example.reflect.databinding.FragmentStatisticsBinding
import com.example.reflect.presentation.common.DayXAxisFormatter
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.common.WeekXAxisFormatter
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.LineChartState
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val vm: VIewModelStatistic by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.lineChartState.collect { state ->
                    handleStatisticState(state)
                }
            }
        }
        with(binding) {
            fragmentStatisticBarChart.setExtraOffsets(4f,20f,4f,10f)
            with (fragmentStatisticBarChart) {
                val testSize = 16f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.BLACK
                xAxis.textSize = testSize
                xAxis.setAvoidFirstLastClipping(true)


                axisRight.isEnabled = false
                axisRight.setDrawZeroLine(false)

                axisLeft.setDrawTopYLabelEntry(false)
                axisLeft.setDrawGridLines(false)
                axisLeft.setDrawZeroLine(true)
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 10f
                axisLeft.textColor = Color.BLACK
                axisLeft.textSize = testSize

                legend.textSize = testSize + 4f
                legend.textColor = ContextCompat.getColor(requireContext(), R.color.onSurface)
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.yEntrySpace = 100f
                legend.formToTextSpace = 14f

                description.isEnabled = false
            }


            fragmentStatisticToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                lifecycleScope.launch {
                    when (checkedId) {
                        R.id.fragmentStatisticWeekButton -> {
                            vm.userIntent.send(StatisticIntent.WeekStatistic)
                            fragmentStatisticBarChart.xAxis.valueFormatter = WeekXAxisFormatter()
                        }
                        R.id.fragmentStatisticMonthButton -> {
                            vm.userIntent.send(StatisticIntent.MonthStatistic)
                            fragmentStatisticBarChart.xAxis.valueFormatter = DayXAxisFormatter()
                        }
                        R.id.fragmentStatisticYearButton -> {
                            vm.userIntent.send(StatisticIntent.YearStatistic)
                            fragmentStatisticBarChart.xAxis.valueFormatter = DayXAxisFormatter()
                        }

                    }
                }
            }
            fragmentStatisticWeekButton.performClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleStatisticState(state: LineChartState) {
        val context = requireContext()
        when (state) {
            is LineChartState.Loading -> {
                Toast.makeText(context, "симуляция загрузки ёу", Toast.LENGTH_SHORT).show()
            }
            is LineChartState.Success -> {
                val dataSet = BarDataSet(state.data, "Среднее значение за период").apply {
                    color = ContextCompat.getColor(context, R.color.primary)
//                    setCircleColors(ContextCompat.getColor(context, R.color.primary))
                    setValueTextColors(mutableListOf(ContextCompat.getColor(context, R.color.primary)))
                    valueTextSize = 9f
//                    highlightLineWidth = 1f
                    highLightColor = ContextCompat.getColor(context, R.color.secondary)
//                    circleRadius = 5f
//                    lineWidth = 3f
                }

                binding.fragmentStatisticBarChart.data = BarData(dataSet)
                binding.fragmentStatisticBarChart.setNoDataText("Добавьте эмоциональное состояние")
                binding.fragmentStatisticBarChart.animateXY(state.data.size * 80, 300)
            }
            is LineChartState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is LineChartState.Idle -> {
                Unit
            }
        }
    }
}