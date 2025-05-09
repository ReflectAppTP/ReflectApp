package com.example.reflect.presentation.common

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DayXAxisFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return value.toString()
    }
}