package com.example.reflect.presentation.screens.statistics.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentStatisticsBinding
import com.example.reflect.domain.model.StatisticTagModel
import com.example.reflect.presentation.adapters.StatisticTagListAdapter
import com.example.reflect.presentation.common.TimeRange
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.common.formatter.LineChartXAxisFormatter
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.states.StatisticTagState
import com.example.reflect.presentation.screens.statistics.states.LineChartState
import com.example.reflect.presentation.screens.statistics.states.PieChartState
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import java.io.File
import java.io.FileOutputStream
import kotlin.math.floor

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val vm: VIewModelStatistic by activityViewModels()

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticFirstTagsAdapter: StatisticTagListAdapter
    private lateinit var statisticSecondTagsAdapter: StatisticTagListAdapter

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
                vm.firstStatisticTagState.collect { state ->
                    handleFirstBarChartState(state, context)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.secondStatisticTagState.collect { state ->
                    handleSecondBarChartState(state, context)
                }
            }
        }

        setLineChartProperties(context)
        setPieChartProperties(context)
        setFirstBarChartProperties(context)
        setSecondBarChartProperties(context)

        statisticFirstTagsAdapter = StatisticTagListAdapter()
        statisticSecondTagsAdapter = StatisticTagListAdapter()

        with(binding) {
            lifecycleScope.launch {
                vm.timeRangeTitle.collect { data ->
                    fragmentStatisticTimeRangeTitle.text = data
                }
            }

            fragmentStatisticCardFirstTagsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            fragmentStatisticCardFirstTagsRV.adapter = statisticFirstTagsAdapter
            fragmentStatisticCardSecondTagsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            fragmentStatisticCardSecondTagsRV.adapter = statisticSecondTagsAdapter

            fragmentStatisticToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    lifecycleScope.launch {
                        when (checkedId) {
                            R.id.fragmentStatisticWeekButton -> {
                                vm.userIntent.send(StatisticIntent.WeekStatistic)
                            }
                            R.id.fragmentStatisticMonthButton -> {
                                vm.userIntent.send(StatisticIntent.MonthStatistic)
                            }
                            R.id.fragmentStatisticYearButton -> {
                                vm.userIntent.send(StatisticIntent.YearStatistic)
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

            fragmentStatisticToolbarExportDataIcon.setOnClickListener {
                if (isDataExportable()) {
                    exportToExcel(context,
                        (vm.lineChartState.value as LineChartState.Success).data,
                        (vm.pieChartState.value as PieChartState.Success).data,
                        (vm.firstStatisticTagState.value as StatisticTagState.Success).data,
                        (vm.secondStatisticTagState.value as StatisticTagState.Success).data,
                    )
                } else Toast.makeText(context, "Невозможно экспортировать статистику", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleLineChartState(state: LineChartState, context: Context) {
        with (binding) {
            when (state) {
                is LineChartState.Loading -> {
                    fragmentStatisticLottieLineChart.visibility = View.VISIBLE
                    fragmentStatisticLineChart.visibility = View.GONE
                }
                is LineChartState.Success -> {
                    fragmentStatisticLottieLineChart.visibility = View.GONE
                    fragmentStatisticLineChart.visibility = View.VISIBLE

                    fragmentStatisticLineChart.data = if (state.data.isEmpty()) null else LineData(
                        LineDataSet(state.data, " ").apply {
                            lineWidth = 5f
                            color = ContextCompat.getColor(context, R.color.tertiary)
                            circleColors = mutableListOf(ContextCompat.getColor(context, R.color.tertiary))
                            circleRadius = 5f
                            circleHoleRadius = 2f
                        }).apply {
                            fragmentStatisticLineChart.xAxis.apply {
                                axisMinimum = xMin
                                axisMaximum = xMax
                                granularity = when(state.timeRange) {
                                    TimeRange.WEEK -> 1f
                                    TimeRange.MONTH -> floor(state.data.size / 5f).coerceAtLeast(1f)
                                    TimeRange.YEAR -> floor(state.data.size / 10f).coerceAtLeast(1f)
                                }
                                labelCount = state.data.size
                                valueFormatter = LineChartXAxisFormatter(state.data.map { it.data.toString() }, state.timeRange)
                            }
                            setDrawValues(false)
                        }
                    fragmentStatisticLineChart.animateX(state.data.size * 80)
                }
                is LineChartState.Error -> {
                    fragmentStatisticLottieLineChart.visibility = View.GONE
                    fragmentStatisticLineChart.visibility = View.VISIBLE
                    fragmentStatisticLineChart.data = null

//                    ToastUtils.showErrorToast(context)
                }
                is LineChartState.Idle -> {
                    Unit
                }
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
        with (binding) {
            when (state) {
                is PieChartState.Loading -> {
                    fragmentStatisticLottiePieChart.visibility = View.VISIBLE
                    fragmentStatisticPieChart.visibility = View.GONE
                }
                is PieChartState.Success -> {
                    fragmentStatisticLottiePieChart.visibility = View.GONE
                    fragmentStatisticPieChart.visibility = View.VISIBLE

                    if (state.data.isEmpty()) {
                        fragmentStatisticPieChart.data = null
                    } else {
                        val dataSetColors = mutableListOf<Int>()
                        for (data in state.data) {
                            dataSetColors.add(pieColors[(data.data as Int) - 1])
                        }
                        fragmentStatisticPieChart.data = PieData(PieDataSet(state.data, "").apply {
                            colors = dataSetColors
                            sliceSpace = 5f
                            selectionShift = 5f
                        }).apply {
                            setDrawValues(false)
                        }
                    }
                    fragmentStatisticPieChart.animateX(state.data.size * 100)
                }
                is PieChartState.Error -> {
                    fragmentStatisticLottiePieChart.visibility = View.GONE
                    fragmentStatisticPieChart.visibility = View.VISIBLE
                    fragmentStatisticPieChart.data = null

//                    ToastUtils.showErrorToast(context)
                }
                is PieChartState.Idle -> {
                    Unit
                }
            }
        }
    }

    private fun handleFirstBarChartState(state: StatisticTagState, context: Context) {
        with (binding) {
            when (state) {
                is StatisticTagState.Loading -> {
                    statisticFirstTagsAdapter.submitList(null)

                    fragmentStatisticLottieFirstTagsChart.visibility = View.VISIBLE
                    fragmentStatisticFirstRVGroup.visibility = View.GONE
                    fragmentStatisticFirstBarChart.visibility = View.GONE
                }
                is StatisticTagState.Success -> {
                    fragmentStatisticLottieFirstTagsChart.visibility = View.GONE

                    if (state.data.isEmpty()) {
                        fragmentStatisticFirstRVGroup.visibility = View.GONE
                        fragmentStatisticFirstBarChart.visibility = View.VISIBLE
                    } else {
                        fragmentStatisticFirstRVGroup.visibility = View.VISIBLE
                        fragmentStatisticFirstBarChart.visibility = View.GONE

                        statisticFirstTagsAdapter.submitList(null)
                        statisticFirstTagsAdapter.submitList(state.data.toMutableList())
                    }
                    fragmentStatisticFirstBarChart.animateX(state.data.size * 100)
                }
                is StatisticTagState.Error -> {
                    fragmentStatisticLottieFirstTagsChart.visibility = View.GONE
                    fragmentStatisticFirstRVGroup.visibility = View.GONE
                    fragmentStatisticFirstBarChart.visibility = View.VISIBLE

//                    ToastUtils.showErrorToast(context)
                }
                is StatisticTagState.Idle -> {
                    Unit
                }
            }
        }
    }

    private fun handleSecondBarChartState(state: StatisticTagState, context: Context) {
        with (binding) {
            when (state) {
                is StatisticTagState.Loading -> {
                    statisticSecondTagsAdapter.submitList(null)
                    fragmentStatisticLottieSecondTagsChart.visibility = View.VISIBLE
                    fragmentStatisticSecondRVGroup.visibility = View.GONE
                    fragmentStatisticSecondBarChart.visibility = View.GONE
                }
                is StatisticTagState.Success -> {
                    fragmentStatisticLottieSecondTagsChart.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        fragmentStatisticSecondRVGroup.visibility = View.GONE
                        fragmentStatisticSecondBarChart.visibility = View.VISIBLE
                    } else {
                        fragmentStatisticSecondRVGroup.visibility = View.VISIBLE
                        fragmentStatisticSecondBarChart.visibility = View.GONE

                        statisticSecondTagsAdapter.submitList(null)
                        statisticSecondTagsAdapter.submitList(state.data.toMutableList())
                    }
                    fragmentStatisticSecondBarChart.animateX(state.data.size * 100)
                }
                is StatisticTagState.Error -> {
                    fragmentStatisticLottieSecondTagsChart.visibility = View.GONE
                    fragmentStatisticSecondRVGroup.visibility = View.GONE
                    fragmentStatisticSecondBarChart.visibility = View.VISIBLE

                    ToastUtils.showErrorToast(context)
                }
                is StatisticTagState.Idle -> {
                    Unit
                }
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

    private fun setPieChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticPieChart) {
                setExtraOffsets(8f,0f,4f,0f)

                centerText = context.resources.getString(R.string.fragmentStatisticStateFrequency)
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

                setNoDataText(context.resources.getString(R.string.fragmentStatisticEmptyChartData))
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 60f
                    color = ContextCompat.getColor(context, R.color.onSurface)
                }
                invalidate()
            }
        }
    }

    private fun setFirstBarChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticFirstBarChart) {
                data = null
                setNoDataText(context.resources.getString(R.string.fragmentStatisticEmptyChartData))
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 60f
                    color = ContextCompat.getColor(context, R.color.onSurface)
                }
                invalidate()
            }
        }
    }

    private fun setSecondBarChartProperties(context: Context) {
        with (binding) {
            with (fragmentStatisticSecondBarChart) {
                data = null
                setNoDataText(context.resources.getString(R.string.fragmentStatisticEmptyChartData))
                getPaint(Chart.PAINT_INFO).apply {
                    textSize = 60f
                    color = ContextCompat.getColor(context, R.color.onSurface)
                }
                invalidate()
            }
        }
    }

    private fun exportToExcel(
        context: Context,
        lineChartData: List<Entry>,
        pieChartData: List<PieEntry>,
        emotionalTagData: List<StatisticTagModel>,
        tagData: List<StatisticTagModel>,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var i = 0
                val workbook = HSSFWorkbook()
                val sheet = when (vm.time.value) {
                    TimeRange.WEEK -> workbook.createSheet("Статистика за неделю")
                    TimeRange.MONTH -> workbook.createSheet("Статистика за месяц")
                    TimeRange.YEAR -> workbook.createSheet("Статистика за год")
                }
                sheet.defaultColumnWidth = 15

                val lineChartTitle = sheet.createRow(i++).apply {
                    createCell(0).apply {
                        setCellValue("Статистика")
                    }
                }
                val lineChartHeader = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Дата")
                    createCell(1).setCellValue("Среднее значение")
                }
                lineChartData.forEachIndexed { index, entry ->
                    val row = sheet.createRow(index +  i)
                    row.createCell(0).setCellValue(entry.data.toString())
                    row.createCell(1).setCellValue(entry.y.toDouble())
                }
                i += lineChartData.size + 1

                val pieChartTitle = sheet.createRow(i++).apply {
                    createCell(0).apply {
                        setCellValue("Частота настроения")
                    }
                }
                val pieChartHeader = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Частота")
                    createCell(1).setCellValue("Настроение")
                }
                val sum = pieChartData.map { it.value }.sum()
                pieChartData.forEachIndexed { index, pieEntry ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue((pieEntry.value / sum * 100).toInt().toString() + " %")
                    row.createCell(1).setCellValue(pieEntry.label)
                }
                i += pieChartData.size + 1

                val emotionalTagTitle = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Статистика по эмоциональным тэгам")
                }
                val emotionalTagHeader = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Название тэга")
                    createCell(1).setCellValue("Эмодзи")
                    createCell(2).setCellValue("Частота")
                }

                emotionalTagData.forEachIndexed { index, model ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue(model.name)
                    row.createCell(1).setCellValue(model.emoji)
                    row.createCell(2).setCellValue(model.freq.toDouble())
                }
                i += emotionalTagData.size + 1

                val tagTitle = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Статистика по тэгам")
                }
                val tagHeader = sheet.createRow(i++).apply {
                    createCell(0).setCellValue("Название тэга")
                    createCell(1).setCellValue("Эмодзи")
                    createCell(2).setCellValue("Частота")
                }
                tagData.forEachIndexed { index, model ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue(model.name)
                    row.createCell(1).setCellValue(model.emoji)
                    row.createCell(2).setCellValue(model.freq.toDouble())
                }
                i += tagData.size + 1

                withContext(Dispatchers.Main) {
                    saveExcelFile(context, workbook)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveExcelFile(context: Context, workbook: HSSFWorkbook) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filename = when (vm.time.value) {
                    TimeRange.WEEK -> "week_statistics.xlsx"
                    TimeRange.MONTH -> "month_statistics.xlsx"
                    TimeRange.YEAR -> "year_statistics.xlsx"
                }
                val filePath = File(context.getExternalFilesDir(null), filename)
                val fileOutputStream = FileOutputStream(filePath)
                workbook.write(fileOutputStream)
                fileOutputStream.close()
                workbook.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Excel файл сохранен: ${filePath.absolutePath}", Toast.LENGTH_SHORT).show()
                    Log.d("Ok excel", "Excel файл сохранен: ${filePath.absolutePath}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ошибка при сохранении файла: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun isDataExportable(): Boolean {
        return vm.lineChartState.value is LineChartState.Success 
                && vm.pieChartState.value is PieChartState.Success
                && vm.firstStatisticTagState.value is StatisticTagState.Success
                && vm.secondStatisticTagState.value is StatisticTagState.Success
    }
}