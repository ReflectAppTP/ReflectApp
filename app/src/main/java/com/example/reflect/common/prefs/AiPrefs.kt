package com.example.reflect.common.prefs

import android.content.Context
import android.content.SharedPreferences

object AiPrefs {
    private const val PREFS_NAME = "ai_prefs"
    private const val IS_AGREED = "agreed"

    private fun getPrefs(context: Context): SharedPreferences
        = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isAgreed(context: Context): Boolean =
        getPrefs(context).getBoolean(IS_AGREED, false)

    fun setAgree(context: Context, agree: Boolean) {
        getPrefs(context).edit().putBoolean(IS_AGREED, agree).apply()
    }

    fun clearPrefs(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}