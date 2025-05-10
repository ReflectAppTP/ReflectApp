package com.example.reflect.presentation.common.formatter

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class TagXAxisFormatter(private val labels: List<String>): ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return labels.getOrNull(value.toInt() - 1) ?: ""
    }
}