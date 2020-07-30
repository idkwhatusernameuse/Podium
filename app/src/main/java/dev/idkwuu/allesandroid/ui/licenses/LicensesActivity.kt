package dev.idkwuu.allesandroid.ui.licenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.util.LicenseExtractor

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)

        // Back button
        findViewById<ImageButton>(R.id.back).setOnClickListener { finish() }

        // Set up recycler view with licenses
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = LicensesAdapter(this, LicenseExtractor().getLicenses(this))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}