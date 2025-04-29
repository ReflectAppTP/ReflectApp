package com.example.reflect.common

import android.content.Context
import android.content.SharedPreferences
import com.example.reflect.domain.model.UserModel
import com.google.gson.Gson

object AccountPrefs {
    private const val PREFS_NAME = "auth_prefs"
    private const val LOG_STATE = "is_logged_in"
    private const val GUEST_STATE = "is_guest"
    private const val AUTH_TOKEN = "auth_token"
    private const val USER = "user"

    private fun getPrefs(context: Context) : SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthState(context: Context, isLoggedIn: Boolean, token: String?, isGuest: Boolean = false, user: UserModel) {
        getPrefs(context).edit().apply{
            putBoolean(GUEST_STATE, isGuest)
            putBoolean(LOG_STATE, isLoggedIn)
            putString(AUTH_TOKEN, token)
            putString(USER, Gson().toJson(user))
            apply()
        }
    }

    fun isAuthorized(context: Context) = getPrefs(context).getBoolean(LOG_STATE, false)

    fun isGuest(context: Context) = getPrefs(context).getBoolean(GUEST_STATE, false)

    fun isLoggedIn(context: Context) = isAuthorized(context) || isGuest(context)

    fun getAuthToken(context: Context) = getPrefs(context).getString(AUTH_TOKEN, null)

    // TODO: Возможно, переделать catch блок
    fun getUser(context: Context) =
        try {
            Gson().fromJson(getPrefs(context).getString(USER, "Надо было покрыть это тестами"), UserModel::class.java)
        } catch (e: Exception) {
            UserModel(-1,"", "","", isAdmin = false, isPremium = false)
        }

    fun clearAuthState(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
