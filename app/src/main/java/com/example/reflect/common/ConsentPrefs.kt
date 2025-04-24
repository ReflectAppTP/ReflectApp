package com.example.reflect.common

import android.content.Context
import android.content.SharedPreferences

object ConsentPrefs {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_CONSENT = "consent_given"

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun hasConsent(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_CONSENT, false)

    fun setConsent(context: Context, consent: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_CONSENT, consent).apply()
    }
}