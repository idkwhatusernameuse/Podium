package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import dev.idkwuu.allesandroid.BuildConfig
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.licenses.LicensesActivity
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.switchTheme

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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

        // Licenses
        findViewById<CardView>(R.id.licenses).setOnClickListener {
            startActivity(Intent(this, LicensesActivity::class.java))
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
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PodiumApp_")))
            }
            .setNegativeButton("GitHub") { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/idkwhatusernameuse/Podium")))
            }
            .setNeutralButton(R.string.close) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        builder.show()
    }
}