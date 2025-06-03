package com.example.reflect.presentation.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.reflect.R

object ToastUtils {
    private const val YOFFSET = 280
    // TODO: Переделать, потому что deprecated (христа ради, почему?)
    fun showAddStateToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_add_state, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showEditStateToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_edit_state, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showDeleteStateToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_delete_state, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showErrorToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_error, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showWelcomeToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_welcome, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showSuccessfulRegistrationToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_successful_registration, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showLoadingToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_loading, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showErrorConnectionToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_error_connection, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }


    fun showErrorExportStatisticToast(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_error_atistic_export, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showResetContextAI(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_reset_context_ai, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showAddWidget(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_add_widget, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun showSendReport(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_send_report, null)
        val toast = Toast(context).apply {
            setView(view)
            setGravity(Gravity.BOTTOM, 0,YOFFSET)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }
}