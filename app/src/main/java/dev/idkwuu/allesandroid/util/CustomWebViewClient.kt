package dev.idkwuu.allesandroid.util

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(private val context: Context): WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request!!.url.host == "alles.cx") {
            return false
            // TODO: Open MainActivity when logged in
        }
        context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
        return true
    }
}