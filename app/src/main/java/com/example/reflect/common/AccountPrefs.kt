package com.example.reflect.common

import android.content.Context
import android.content.SharedPreferences

object AccountPrefs {
    private const val PREFS_NAME = "auth_prefs"
    private const val LOG_STATE = "is_logged_in"
    private const val GUEST_STATE = "is_guest"
    private const val AUTH_TOKEN = "auth_token"
    private const val USER_LOGIN = "user_login"

    private fun getPrefs(context: Context) : SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthState(context: Context, isLoggedIn: Boolean, token: String?, isGuest: Boolean = false, userLogin: String) {
        getPrefs(context).edit().apply{
            putBoolean(GUEST_STATE, isGuest)
            putBoolean(LOG_STATE, isLoggedIn)
            putString(AUTH_TOKEN, token)
            putString(USER_LOGIN, userLogin)
            apply()
        }
    }

    fun isAuthorized(context: Context) = getPrefs(context).getBoolean(LOG_STATE, false)

    fun isGuest(context: Context) = getPrefs(context).getBoolean(GUEST_STATE, false)

    fun isLoggedIn(context: Context) = isAuthorized(context) || isGuest(context)

    fun getAuthToken(context: Context) = getPrefs(context).getString(AUTH_TOKEN, null)

    // TODO: Переделать userLogin
    fun getUserLogin(context: Context) = getPrefs(context).getString(USER_LOGIN, "Надо было покрыть это тестами")

    fun clearAuthState(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
