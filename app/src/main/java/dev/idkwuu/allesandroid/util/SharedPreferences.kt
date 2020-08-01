package dev.idkwuu.allesandroid.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dev.idkwuu.allesandroid.BuildConfig

object SharedPreferences {

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var login_preferences: SharedPreferences

    fun init(context: Context) {
        if (!this::preferences.isInitialized || !this::login_preferences.isInitialized) {
            preferences = context.getSharedPreferences("dev.idkwuu.allesandroid.preferences", MODE)
            login_preferences = EncryptedSharedPreferences.create(
                "dev.idkwuu.allesandroid.securepreferences",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
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
    private const val key_bookmarks = "bookmarks"

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

    var current_user: String?
        get() = login_preferences.getString(key_currentUser, "")
        set(value) = login_preferences.edit{
            it.putString(key_currentUser, value)
        }

    var theme: Int
        get() = preferences.getInt(key_theme, 0)
        set(value) = preferences.edit {
            it.putInt(key_theme, value)
        }

    var bookmarks: String
        get() = preferences.getString(key_bookmarks, "[]") ?: "[]"
        set(value) = preferences.edit{
            it.putString(key_bookmarks, value)
        }

}