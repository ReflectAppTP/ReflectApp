package com.example.reflect.presentation.screens.statistics.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import com.example.reflect.presentation.common.formatter.DayXAxisFormatter
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.common.formatter.TagXAxisFormatter
import com.example.reflect.presentation.common.formatter.WeekXAxisFormatter
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.states.BarChartState
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.example.reflect.presentation.screens.statistics.states.PieChartState
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
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
        val context = requireContext()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.lineChartState.collect { state ->
                    handleLineChartState(state, context)
                }
            }
        }

        // TODO: Сделать handle для премиума
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.pieChartState.collect { state ->
                    handlePieChartState(state, context)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.firstBarChartState.collect { state ->
                    handleFirstBarChartState(state, context)
                }
            }
        }

        setLineChartProperties(context)
        setPieChartProperties(context)
        setFirstBarChartProperties(context)

        with(binding) {
            fragmentStatisticToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    lifecycleScope.launch {
                        when (checkedId) {
                            R.id.fragmentStatisticWeekButton -> {
                                vm.userIntent.send(StatisticIntent.WeekStatistic)
                                fragmentStatisticLineChart.xAxis.valueFormatter = WeekXAxisFormatter()
                            }
                            R.id.fragmentStatisticMonthButton -> {
                                vm.userIntent.send(StatisticIntent.MonthStatistic)
                                fragmentStatisticLineChart.xAxis.valueFormatter = DayXAxisFormatter()
                            }
                            R.id.fragmentStatisticYearButton -> {
                                vm.userIntent.send(StatisticIntent.YearStatistic)
                                fragmentStatisticLineChart.xAxis.valueFormatter = DayXAxisFormatter()
                            }
                        }
                    }
                } else {
                    if (-1 == group.checkedButtonId) {
                        group.check(checkedId)
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

    private fun handleLineChartState(state: LineChartState, context: Context) {
        when (state) {
            is LineChartState.Loading -> {
                Toast.makeText(context, "симуляция загрузки ёу", Toast.LENGTH_SHORT).show()
            }
            is LineChartState.Success -> {
                binding.fragmentStatisticLineChart.data = if (state.data.isEmpty()) null else LineData(
                    LineDataSet(state.data, "Среднее значение за период").apply {
                        lineWidth = 5f
                        color = ContextCompat.getColor(context, R.color.tertiary)
                        circleColors = mutableListOf(ContextCompat.getColor(context, R.color.tertiary))
                        circleRadius = 5f
                        circleHoleRadius = 2f
//                        valueTextSize = 9f
//                        highLightColor = ContextCompat.getColor(context, R.color.tertiary)
                }).apply { setDrawValues(false) }
                binding.fragmentStatisticLineChart.animateX(state.data.size * 80)
            }
            is LineChartState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is LineChartState.Idle -> {
                Unit
            }
        }
    }

    private fun handlePieChartState(state: PieChartState, context: Context) {
        // TODO: ГОВНОКОД! Лучше куда то вынести
        val pieColors = listOf(
            ContextCompat.getColor(context, R.color.pieChartStateAwful),
            ContextCompat.getColor(context, R.color.pieChartStateBad),
            ContextCompat.getColor(context, R.color.pieChartStateNormal),
            ContextCompat.getColor(context, R.color.pieChartStateNice),
            ContextCompat.getColor(context, R.color.pieChartStateMagnifique),
        )
        when (state) {
            is PieChartState.Loading -> {
                Toast.makeText(context, "симуляция загрузки ёу", Toast.LENGTH_SHORT).show()
            }
            is PieChartState.Success -> {
                if (state.data.isEmpty()) {
                    binding.fragmentStatisticPieChart.data = null
                } else {
                    val dataSetColors = mutableListOf<Int>()
                    for (data in state.data) {
                        dataSetColors.add(pieColors[(data.data as Int) - 1])
                    }
                    binding.fragmentStatisticPieChart.data = PieData(PieDataSet(state.data, "").apply {
                        colors = dataSetColors
//                        valueTextSize = 0f
//                        valueTextColor = ContextCompat.getColor(context, R.color.onSurface)
                        sliceSpace = 5f
                        selectionShift = 5f
                    }).apply {
//                        setValueFormatter(PercentFormatter(binding.fragmentStatisticPieChart))
                        setDrawValues(false)
                    }
                }
//                binding.fragmentStatisticPieChart.data = if (state.data.isEmpty()) null else PieData(
//                    PieDataSet(state.data, "").apply {
//                        colors = mutableListOf(
//                            ContextCompat.getColor(context, R.color.tertiary),
//                            ContextCompat.getColor(context, R.color.primary),
//                            ContextCompat.getColor(context, R.color.secondary),
//                            ContextCompat.getColor(context, R.color.error),
//                            ContextCompat.getColor(context, R.color.onErrorContainer),
//                        )
//                        valueTextSize = 14f
//                        valueTextColor = ContextCompat.getColor(context, R.color.surfaceContainerLow)
//                        sliceSpace = 3f
//                        selectionShift = 5f
//                    }
//                ).apply { }
                binding.fragmentStatisticPieChart.animateX(state.data.size * 100)
            }
            is PieChartState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is PieChartState.Idle -> {
                Unit
            }
        }
    }

    private fun handleFirstBarChartState(state: BarChartState, context: Context) {
        when (state) {
            is BarChartState.Loading -> {
                Toast.makeText(context, "симуляция загрузки ёу", Toast.LENGTH_SHORT).show()
            }
            is BarChartState.Success -> {
                val colors = listOf(
                    Color.RED,
                    Color.rgb(255, 165, 0),
                    Color.YELLOW,
                    Color.GREEN,
                    Color.CYAN
                )
                with (binding ){
                    if (state.data.isEmpty()) {
                        fragmentStatisticFirstBarChart.data = null
                    } else {
                        fragmentStatisticFirstBarChart.data = BarData(BarDataSet(state.data, "").apply {
                            valueTextSize = 6f
                        })
                    }
                    fragmentStatisticFirstBarChart.animateX(state.data.size * 100)
                }
            }
            is BarChartState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is BarChartState.Idle -> {
                Unit
            }
        }
    }

    private fun handleSecondBarChartState(state: BarChartState, context: Context) {
        when (state) {
            is BarChartState.Loading -> {
                Toast.makeText(context, "симуляция загрузки ёу", Toast.LENGTH_SHORT).show()
            }
            is BarChartState.Success -> {

            }
            is BarChartState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is BarChartState.Idle -> {
                Unit
            }
        }
    }

    private fun setLineChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticLineChart) {
                setExtraOffsets(20f,20f,20f,20f)
                isDoubleTapToZoomEnabled = false
                setTouchEnabled(false)

                val testSize = 16f
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = ContextCompat.getColor(context, R.color.onSurface)
                    textSize = testSize
                    setDrawGridLines(true)
                    gridColor = ContextCompat.getColor(context, R.color.onSurface)
                }

                axisRight.apply {
//                    isEnabled = false
                    setDrawTopYLabelEntry(false)
                    setDrawZeroLine(true)
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    gridColor = ContextCompat.getColor(context, R.color.onSurface)
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
//                legend.apply {
//                    textSize = testSize + 6f
//                    textColor = ContextCompat.getColor(context, R.color.onSurface)
//                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                    yEntrySpace = 100f
//                    formToTextSpace = 12f
//                }

                description.isEnabled = false

                setNoDataText("Пока что здесь пусто")
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 56f
                    color = ContextCompat.getColor(context, R.color.primary)
                }
                invalidate()
            }
        }
    }

    private fun setPieChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticPieChart) {
                setExtraOffsets(8f,0f,4f,0f)

                centerText = "Частота настроения"
                setCenterTextTypeface(Typeface.createFromAsset(context.assets, "fonts/InterSemiBold.ttf"))
                setCenterTextSize(14f)

                description.isEnabled = false

                setUsePercentValues(true)
                
                setDrawEntryLabels(false)
                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    orientation = Legend.LegendOrientation.VERTICAL
                    isEnabled = true
                    form = Legend.LegendForm.CIRCLE
                    textSize = 14f
                    yOffset = -35f
                    typeface = Typeface.createFromAsset(context.assets, "fonts/InterRegular.ttf")
                    textColor = ContextCompat.getColor(context, R.color.onSurfaceVariant)
                }

                transparentCircleRadius = 50f

                setNoDataText("Пока что здесь пусто")
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 56f
                    color = ContextCompat.getColor(context, R.color.primary)
                }
                invalidate()
            }
        }
    }

    private fun setFirstBarChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticFirstBarChart) {
                fitScreen()
                setExtraOffsets(-10f,6f,-10f,20f)
//                setFitBars(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    valueFormatter = TagXAxisFormatter(vm.firstBarChartLabels.value)
                }

                axisLeft.isEnabled = false
                axisRight.isEnabled = false

                legend.isEnabled = false
                description.isEnabled = false

                setNoDataText("Пока что здесь пусто")
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 56f
                    color = ContextCompat.getColor(context, R.color.primary)
                }
                invalidate()
            }
        }
    }
}