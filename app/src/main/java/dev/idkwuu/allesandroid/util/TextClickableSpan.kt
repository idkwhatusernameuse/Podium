package dev.idkwuu.allesandroid.util

import android.content.Intent
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import dev.idkwuu.allesandroid.ui.profile.ProfileActivity
import dev.idkwuu.allesandroid.ui.ThreadActivity

class TextClickableSpan(string: String) : ClickableSpan() {
    val text = string

    override fun onClick(view: View) {
        // Handle the opening of usernames, hashtags and posts.
        lateinit var intent: Intent
        when {
            text.contains('@') -> {
                intent = Intent(view.context, ProfileActivity::class.java)
                intent.putExtra("user", text.replace("@", ""))
            }
            text.contains('#') -> {

            }
            text.contains('%') -> {
                intent = Intent(view.context, ThreadActivity::class.java)
                intent.putExtra("post", text.replace("%", ""))
            }
        }
        // Do not try to open hastags, yet.
        if (!text.contains('#')) {
            startActivity(view.context, intent, null)
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = true
    }
}