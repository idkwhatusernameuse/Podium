package dev.idkwuu.allesandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.profile.ProfileFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ProfileFragment().newInstance(
            user = intent.getStringExtra("user").toString(),
            withBackButton = true
        )

        fragmentTransaction.add(R.id.main, fragment!!)
        fragmentTransaction.commit()
    }
}