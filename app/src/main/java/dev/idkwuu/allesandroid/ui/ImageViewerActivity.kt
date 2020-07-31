package dev.idkwuu.allesandroid.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.ortiz.touchview.TouchImageView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.util.ImageUtils

class ImageViewerActivity : AppCompatActivity() {
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer_activty)

        val imageView = findViewById<TouchImageView>(R.id.photo_view)
        val download = findViewById<ImageButton>(R.id.download)
        //Image
        Glide.with(this)
            .asBitmap()
            .load(intent.getStringExtra("URL"))
            .into(object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    imageView.setImageBitmap(resource)
                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    download.visibility = View.VISIBLE
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
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PackageManager.PERMISSION_GRANTED)
            } else {
                saveImage()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PackageManager.PERMISSION_GRANTED -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    saveImage()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    val builder = AlertDialog.Builder(this)
                        .setTitle(R.string.storage_permission)
                        .setMessage(R.string.storage_permission_why)
                        .setPositiveButton(R.string.continue_) { _, _ ->
                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                PackageManager.PERMISSION_GRANTED)
                        }
                        .setNegativeButton(R.string.cancel) { _, _ ->
                        }
                        .create()
                    builder.show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun saveImage() {
        val savingSnackbar = Snackbar.make(findViewById<FrameLayout>(R.id.main), R.string.saving_image, Snackbar.LENGTH_LONG)
        savingSnackbar.show()
        if (ImageUtils.saveImage(this, bitmap!!)) {
            // Image was saved
            savingSnackbar.dismiss()
            Snackbar.make(findViewById<FrameLayout>(R.id.main), R.string.saved_image, Snackbar.LENGTH_LONG).show()
        } else {
            // Image wasn't saved
            savingSnackbar.dismiss()
            Snackbar.make(findViewById<FrameLayout>(R.id.main), R.string.couldnt_save_image, Snackbar.LENGTH_LONG).show()
        }
    }
}