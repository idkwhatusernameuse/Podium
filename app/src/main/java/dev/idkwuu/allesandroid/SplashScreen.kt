package dev.idkwuu.allesandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import dev.idkwuu.allesandroid.util.SharedPreferences

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferences.init(this@SplashScreen.applicationContext)

        // Check if user is logged in
        if (SharedPreferences.isLoggedIn) {
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
        }
        // Close splash screen
        finish()
    }
}