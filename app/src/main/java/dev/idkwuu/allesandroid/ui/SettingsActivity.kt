package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.idkwuu.allesandroid.BuildConfig
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.components.ListItem
import dev.idkwuu.allesandroid.ui.components.Subtitle
import dev.idkwuu.allesandroid.ui.licenses.LicensesActivity
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.Theme

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)
        setContent {
            val context = ContextAmbient.current
            AppTheme {
                Surface {
                    Column {
                        Subtitle(context.getString(R.string.app_preferences))
                        ListItem(
                            title = context.getString(R.string.theme),
                            subtitle = context.getString(when(Theme.get()) {
                                1 -> R.string.theme_light
                                2 -> R.string.theme_dark
                                else -> R.string.theme_default
                            }),
                            vectorIcon = R.drawable.ic_fluent_color_24_filled,
                            onClick = { openThemeDialog() })
                        Subtitle(context.getString(R.string.about))
                        ListItem(
                            title = context.getString(R.string.licenses),
                            subtitle = context.getString(R.string.licenses_secondary),
                            vectorIcon = R.drawable.ic_fluent_info_24_filled,
                            onClick = { startActivity(Intent(context, LicensesActivity::class.java)) })
                        ListItem(
                            title = context.getString(R.string.app_info),
                            subtitle = "Podium ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                            imageIcon = R.drawable.ic_logo_round,
                            onClick = { openAppInfo() })
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun openThemeDialog() {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.theme)
            .setItems(R.array.theme_entries) { _, i ->
                Theme.switch(i)
                Toast.makeText(this, R.string.theme_restart, Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        builder.show()
    }

    private fun openAppInfo() {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.app_info)
            .setMessage(R.string.app_info_message)
            .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round))
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