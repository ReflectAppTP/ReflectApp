package com.example.reflect.common

import android.view.View
import com.google.android.material.textfield.TextInputEditText

object Utils {
    fun isEditTextEmpty(et: TextInputEditText) = et.text.toString().trim().isEmpty()
}