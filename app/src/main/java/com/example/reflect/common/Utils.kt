package com.example.reflect.common

import com.google.android.material.textfield.TextInputEditText

object Utils {
    fun isEditTextEmpty(et: TextInputEditText) = et.text.toString().trim().isEmpty()
}