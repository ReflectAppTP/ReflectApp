package com.example.reflect.common.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.reflect.domain.model.UserModel
import com.google.gson.Gson

object AccountPrefs {
    private const val PREFS_NAME = "auth_prefs"
    private const val LOG_STATE = "is_logged_in"
    private const val GUEST_STATE = "is_guest"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val USER = "user"

    private fun getPrefs(context: Context) : SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthState(context: Context, isLoggedIn: Boolean, isGuest: Boolean = false) {
        getPrefs(context).edit().apply{
            putBoolean(GUEST_STATE, isGuest)
            putBoolean(LOG_STATE, isLoggedIn)
            apply()
        }
    }
    
    fun saveUserModel(context: Context, user: UserModel) {
        getPrefs(context).edit().apply{
            putString(USER, Gson().toJson(user))
            apply()
        }
    }
    
    fun saveUserToken(context: Context, accessToken: String, refreshToken: String) {
        getPrefs(context).edit().apply{
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun isAuthorized(context: Context) = getPrefs(context).getBoolean(LOG_STATE, false)

    fun isGuest(context: Context) = getPrefs(context).getBoolean(GUEST_STATE, false)

    fun isLoggedIn(context: Context) = isAuthorized(context) || isGuest(context)

    fun isPremium(context: Context) = getUser(context).isPremium

    fun getAuthToken(context: Context) = getPrefs(context).getString(ACCESS_TOKEN, null)

    fun getRefreshToken(context: Context) = getPrefs(context).getString(REFRESH_TOKEN, null)

    // TODO: Возможно, переделать catch блок
    fun getUser(context: Context): UserModel =
        try {
            Gson().fromJson(getPrefs(context).getString(USER, "Надо было покрыть это тестами"), UserModel::class.java)
        } catch (e: Exception) {
            UserModel(-1,"", "","", isAdmin = false, isPremium = false)
        }

    fun clearTokens(context: Context) {
        getPrefs(context).edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .apply()
    }

    fun clearAuthState(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
