package dev.idkwuu.allesandroid.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("dev.idkwuu.allesandroid.preferences", MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val key_isLoggedIn = "isLoggedIn"
    private const val key_loginToken = "login_token"
    private const val key_currentUser = "current_user"
    private const val key_theme = "theme"

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(key_isLoggedIn, false)
        set(value) = preferences.edit{
            it.putBoolean(key_isLoggedIn, value)
        }

    var login_token: String?
        get() = preferences.getString(key_loginToken, "")
        set(value) = preferences.edit{
            it.putString(key_loginToken, value)
        }

    var current_user: String?
        get() = preferences.getString(key_currentUser, "")
        set(value) = preferences.edit{
            it.putString(key_currentUser, value)
        }

    var theme: Int
        get() = preferences.getInt(key_theme, 0) ?: 0
        set(value) = preferences.edit {
            it.putInt(key_theme, value)
        }

}