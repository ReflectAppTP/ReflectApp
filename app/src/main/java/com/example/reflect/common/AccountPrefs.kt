package com.example.reflect.common

import android.content.Context
import android.content.SharedPreferences

object AccountPrefs {
    private const val PREFS_NAME = "auth_prefs"
    private const val LOG_STATE = "is_logged_in"
    private const val GUEST_STATE = "is_guest"
    private const val AUTH_TOKEN = "auth_token"

    private fun getPrefs(context: Context) : SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthState(context: Context, isLoggedIn: Boolean, token: String?, isGuest: Boolean = false) {
        getPrefs(context).edit().apply{
            if (isGuest) {
                putBoolean(GUEST_STATE, true)
            } else {
                putBoolean(LOG_STATE, isLoggedIn)
                putString(AUTH_TOKEN, token)
            }
            apply()
        }
    }

    fun isLoggedIn(context: Context) = getPrefs(context).getBoolean(LOG_STATE, false)

    fun getAuthToken(context: Context) = getPrefs(context).getString(AUTH_TOKEN, null)

    fun isGuest(context: Context) = getPrefs(context).getBoolean(GUEST_STATE, false)

    fun clearAuthState(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}