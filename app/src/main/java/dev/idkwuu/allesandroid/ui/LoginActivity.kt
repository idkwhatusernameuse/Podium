package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import dev.idkwuu.allesandroid.databinding.ActivityLoginBinding
import dev.idkwuu.allesandroid.util.CustomWebViewClient

class LoginActivity : AppCompatActivity() {

    private lateinit var v: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(v.root)

        v.loginButton.setOnClickListener {
            openWebview()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebview() {
        //Animation
        val transition = ChangeBounds()
        transition.interpolator = FastOutSlowInInterpolator()
        TransitionManager.beginDelayedTransition(v.main, transition)
        val paramsRoot = v.main.layoutParams
        paramsRoot.height = ViewGroup.LayoutParams.MATCH_PARENT
        v.main.layoutParams = paramsRoot

        v.webView.layoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            1.0f
        )

        v.webView.webViewClient = CustomWebViewClient(this)
        v.webView.settings.javaScriptEnabled = true
        v.webView.loadUrl("https://alles.cx")

        v.title.visibility = View.GONE
        v.titleSmall.visibility = View.VISIBLE
        v.loginButton.visibility = View.GONE
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && v.webView.canGoBack()) {
            v.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun openMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}