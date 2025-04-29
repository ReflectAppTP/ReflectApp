package com.example.reflect.presentation.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.reflect.R

object ToastUtils {
    // TODO: Переделать, потому что deprecated (христа ради, почему?)
    fun showAddStateToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_add_state, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,280)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showWelcomeToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_welcome, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,280)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }
}