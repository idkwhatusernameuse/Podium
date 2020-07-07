package dev.idkwuu.allesandroid.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.profile.ProfileFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bundle = Bundle()
        bundle.putString("user", intent.getStringExtra("user"))
        val fragment = ProfileFragment()
        fragment.arguments = bundle

        /*val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.fragment_profile, null)
        val frameLayout = findViewById<FrameLayout>(R.id.main)
        frameLayout.addView(view)*/
    }
}