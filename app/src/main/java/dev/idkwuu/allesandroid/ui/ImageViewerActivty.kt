package dev.idkwuu.allesandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.ortiz.touchview.TouchImageView
import dev.idkwuu.allesandroid.R

class ImageViewerActivty : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer_activty)

        val imageView = findViewById<TouchImageView>(R.id.photo_view)
        //Image
        Glide.with(this)
            .asBitmap()
            .load(intent.getStringExtra("URL"))
            .into(object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                }
            })

        // Top bar hiding animation
        imageView.setOnClickListener {
            val topBar = findViewById<ConstraintLayout>(R.id.bar)
            val autoTransition = AutoTransition()
            autoTransition.duration = 200
            TransitionManager.beginDelayedTransition(topBar, autoTransition)
            topBar.visibility = if (topBar.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Back button
        findViewById<ImageButton>(R.id.back).setOnClickListener {
            finish()
        }

        // Download button
        findViewById<ImageButton>(R.id.download).setOnClickListener {
            Snackbar.make(findViewById<FrameLayout>(R.id.main), R.string.etasoon, Snackbar.LENGTH_LONG).show()
        }
    }
}