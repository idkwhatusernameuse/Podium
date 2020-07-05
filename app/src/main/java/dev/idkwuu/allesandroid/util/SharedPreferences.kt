package dev.idkwuu.allesandroid.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    private const val NAME = "SharedPreferences"
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
/*
    var theme: String
        get() = preferences.getString(THEME.first, THEME.second)
        set(value) = preferences.edit{
            it.putString(THEME.first, value)
        }

    var template: String
        get() = preferences.getString(TEMPLATE.first, TEMPLATE.second)
        set(value) = preferences.edit{
            it.putString(TEMPLATE.first, value)
        }

    var cache_templates: String
        get() = preferences.getString(CACHE_JSON.first , CACHE_JSON.second)
        set(value) = preferences.edit{
            it.putString(CACHE_JSON.first, value)
        }

    var first_load: Boolean
        get() = preferences.getBoolean(FIRST_LOAD.first, FIRST_LOAD.second)
        set(value) = preferences.edit{
            it.putBoolean(FIRST_LOAD.first, value)
        }

    var white_template: Boolean
        get() = preferences.getBoolean(WHITE_TEMPLATE.first, WHITE_TEMPLATE.second)
        set(value) = preferences.edit{
            it.putBoolean(WHITE_TEMPLATE.first, value)
        }*/
}