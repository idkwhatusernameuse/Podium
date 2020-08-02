package dev.idkwuu.allesandroid.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.ui.bookmarks.BookmarksFragment
import dev.idkwuu.allesandroid.ui.home.HomeFragment
import dev.idkwuu.allesandroid.ui.notifications.NotificationsFragment
import dev.idkwuu.allesandroid.ui.profile.ProfileFragment
import dev.idkwuu.allesandroid.util.SharedPreferences


class MainActivity : AppCompatActivity() {

    private val fragment1: Fragment = HomeFragment()
    private val fragment2: Fragment = NotificationsFragment()
    private val fragment3: Fragment = BookmarksFragment()
    private val fragment4: Fragment = ProfileFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragment1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit()

        /*val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)*/
        Repo.handler.post(Repo.onlineRunnable)
    }

    private val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                }
                R.id.navigation_notifications -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                }
                R.id.navigation_bookmarks -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                }
                R.id.navigation_me -> {
                    fm.beginTransaction().hide(active).show(fragment4).commit()
                    active = fragment4
                }
            }
            true
        }

    override fun onStop() {
        Repo.shouldStopLoop = true
        super.onStop()
    }

    override fun onRestart() {
        Repo.handler.post(Repo.onlineRunnable)
        SharedPreferences.init(this)
        super.onRestart()
    }
}