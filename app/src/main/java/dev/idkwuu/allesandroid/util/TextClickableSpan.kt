package dev.idkwuu.allesandroid.util

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

class TextClickableSpan(string: String) : ClickableSpan() {
    val text = string

    override fun onClick(view: View) {
        // Handle the opening of usernames, hashtags and posts.

        Log.d("string", text)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = true
    }
}