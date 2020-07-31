package dev.idkwuu.allesandroid.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun onStop() {
        Repo.shouldStopLoop = true
        super.onStop()
    }

    override fun onRestart() {
        Repo.shouldStopLoop = false
        Repo.handler.post(Repo.onlineRunnable)
        super.onRestart()
    }

    fun getCtxt(): Context = this
}