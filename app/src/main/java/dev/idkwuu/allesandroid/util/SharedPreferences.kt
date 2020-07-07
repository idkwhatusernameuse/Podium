package dev.idkwuu.allesandroid.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var login_preferences: SharedPreferences
    private lateinit var app_preferences: SharedPreferences

    fun init(context: Context) {
        login_preferences = context.getSharedPreferences("dev.idkwuu.allesandroid.LOGIN", MODE)
        app_preferences = context.getSharedPreferences("dev.idkwuu.allesandroid.APP", MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val key_isLoggedIn = "isLoggedIn"
    private const val key_loginToken = "login_token"
    private const val key_cachedFeed = "cached_feed"
    private const val key_currentUser = "current_user"
    private const val key_profile = "profile"

    var isLoggedIn: Boolean
        get() = login_preferences.getBoolean(key_isLoggedIn, false)
        set(value) = login_preferences.edit{
            it.putBoolean(key_isLoggedIn, value)
        }

    var login_token: String?
        get() = login_preferences.getString(key_loginToken, "")
        set(value) = login_preferences.edit{
            it.putString(key_loginToken, value)
        }

    var cached_feed: String?
        get() = app_preferences.getString(key_cachedFeed, "")
        set(value) = app_preferences.edit{
            it.putString(key_cachedFeed, value)
        }

    var current_user: String?
        get() = login_preferences.getString(key_currentUser, "")
        set(value) = login_preferences.edit{
            it.putString(key_currentUser, value)
        }

    var profile: String?
        get() = app_preferences.getString(key_profile, "")
        set(value) = login_preferences.edit{
            it.putString(key_profile, value)
        }
}