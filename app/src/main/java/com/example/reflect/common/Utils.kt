package com.example.reflect.common

import com.google.android.material.textfield.TextInputEditText

object Utils {
    fun isEditTextEmpty(et: TextInputEditText) = et.text.toString().trim().isEmpty()

    fun isEmailValid(email: String): Boolean {
        return email.matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+".toRegex())
    }
}