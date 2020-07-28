package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dev.idkwuu.allesandroid.BuildConfig
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.switchTheme

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Theme setting
        setThemeSecondaryLine()
        findViewById<CardView>(R.id.theme).setOnClickListener {
            openThemeDialog()
        }

        // Back ImageButton
        findViewById<ImageButton>(R.id.back).setOnClickListener {
            finish()
        }

        // App info
        findViewById<TextView>(R.id.app_info_version).text = "Podium ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        findViewById<CardView>(R.id.app_info).setOnClickListener {
            openAppInfo()
        }
    }

    private fun setThemeSecondaryLine() {
        val themeSecondaryLine = findViewById<TextView>(R.id.theme_secondary_line)
        when (SharedPreferences.theme) {
            1 -> {
                themeSecondaryLine.text = getString(R.string.theme_light)
            }
            2 -> {
                themeSecondaryLine.text = getString(R.string.theme_dark)
            }
            else -> {
                themeSecondaryLine.text = getString(R.string.theme_default)
            }
        }
    }

    private fun openThemeDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.theme)
            .setItems(R.array.theme_entries) { _, i ->
                setThemeSecondaryLine()
                switchTheme(i)
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        builder.show()
    }

    private fun openAppInfo() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.app_info)
            .setMessage(R.string.app_info_message)
            .setIcon(getDrawable(R.mipmap.ic_launcher_round))
            .setPositiveButton("Twitter") { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/idkwuu")))
            }
            .setNegativeButton("Alles") { _, _ ->
                // Ironically I'm going to open the Alles profile in the browser, temporarily
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://alles.cx/idkwuu")))
            }
            .setNeutralButton(R.string.close) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        builder.show()
    }
}