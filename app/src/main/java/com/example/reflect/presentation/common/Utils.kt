package com.example.reflect.presentation.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.reflect.R

object Utils {
    // TODO: Переделать, потому что deprecated (христа ради, почему?)
    fun toast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_add_state, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,280)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }
}