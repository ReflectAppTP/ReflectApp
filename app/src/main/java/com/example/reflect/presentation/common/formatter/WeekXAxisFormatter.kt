package com.example.reflect.presentation.common.formatter

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class WeekXAxisFormatter : ValueFormatter() {
    private val days = listOf("пн","вт","ср","чт","пт","сб","вс")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}