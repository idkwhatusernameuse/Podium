package dev.idkwuu.allesandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.switchTheme

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferences.init(this@SplashScreen.applicationContext)
        switchTheme(SharedPreferences.theme)
        if (intent.getBooleanExtra("logout", false)) {
            SharedPreferences.login_token = ""
            SharedPreferences.isLoggedIn = false
            SharedPreferences.current_user = ""
        }
        // Check if user is logged in
        if (SharedPreferences.login_token != "") {
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
        }
        // Close splash screen
        finish()
    }
}