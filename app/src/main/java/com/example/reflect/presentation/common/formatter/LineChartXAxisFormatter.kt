package com.example.reflect.presentation.common.formatter

import com.example.reflect.presentation.common.TimeRange
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class LineChartXAxisFormatter(private val dates: List<String>, private val timeRange: TimeRange) : ValueFormatter() {
    private val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        if (index < 0 || index >= dates.size) return ""

        val dateStr = dates[index]
        val date = sdfInput.parse(dateStr) ?: return dateStr

        return when (timeRange) {
            TimeRange.WEEK -> SimpleDateFormat("EEE", Locale.getDefault()).format(date)
            else -> SimpleDateFormat("d", Locale.getDefault()).format(date)
        }
    }
}