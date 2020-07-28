package dev.idkwuu.allesandroid.util

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

fun switchTheme(index: Int) {
    SharedPreferences.theme = index
    when (index) {
        1 -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        2 -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
    }
}