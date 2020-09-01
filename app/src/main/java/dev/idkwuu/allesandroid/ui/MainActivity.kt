package dev.idkwuu.allesandroid.ui

import android.os.Bundle
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

    private val fragmentsList = listOf(
        HomeFragment(),
        NotificationsFragment(),
        BookmarksFragment(),
        ProfileFragment()
    )
    private var createdFragments = mutableListOf<Boolean>(true, false, false, false)
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentsList[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentsList[0], "1").commit()

        /*val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)*/
        //Repo.handler.post(Repo.onlineRunnable)
    }

    private val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(0)
                }
                R.id.navigation_notifications -> {
                    replaceFragment(1)
                }
                R.id.navigation_bookmarks -> {
                    replaceFragment(2)
                }
                R.id.navigation_me -> {
                    replaceFragment(3)
                }
            }
            true
        }

    private fun replaceFragment(id: Int) {
        if (!createdFragments[id]) {
            createdFragments[id] = true
            fm.beginTransaction().add(R.id.nav_host_fragment, fragmentsList[id], "2").hide(active).commit()
        } else {
            fm.beginTransaction().hide(active).show(fragmentsList[id]).commit()
        }
        active = fragmentsList[id]
    }

    override fun onStop() {
        Repo.shouldStopLoop = true
        super.onStop()
    }

    override fun onRestart() {
        //Repo.handler.post(Repo.onlineRunnable)
        SharedPreferences.init(this)
        super.onRestart()
    }
}