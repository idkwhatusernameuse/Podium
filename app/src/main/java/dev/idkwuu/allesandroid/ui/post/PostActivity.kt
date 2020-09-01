package dev.idkwuu.allesandroid.ui.post

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.databinding.ActivityPostBinding
import dev.idkwuu.allesandroid.models.AllesInteractionPost
import dev.idkwuu.allesandroid.util.ImageUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PostActivity : AppCompatActivity() {

    private lateinit var v: ActivityPostBinding

    private val randomStrings = listOf(
        R.string.post_hint_1,
        R.string.post_hint_2,
        R.string.post_hint_3,
        R.string.post_hint_4,
        R.string.post_hint_5
    )

    private var bitmap: Bitmap? = null
    private var mime: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityPostBinding.inflate(layoutInflater)
        setContentView(v.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Replying to
        val postToReply = intent.getStringExtra("replyTo")
        if (postToReply != null) {
            supportActionBar?.title = "${getString(R.string.replying)} @${intent.getStringExtra("userToReply")}"
            v.post.text = getString(R.string.reply)
        }

        // Get a random phrase for the hint
        randomStringForHint()

        v.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIfCanPost()
                if (count == 0) randomStringForHint()
            }
        })

        v.addImage.setOnClickListener {
            if (bitmap == null) getContent.launch("image/*")
        }

        v.post.setOnClickListener {
            postEverything(postToReply)
        }

        // Remove photo button
        v.removePhoto.setOnClickListener {
            bitmap = null
            mime = ""
            v.addImage.setColorFilter(getColor(R.color.neutral))
            v.imageContainer.visibility = View.GONE
            checkIfCanPost()
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

    private fun checkIfCanPost() {
        if (v.editText.text.toString().isNotEmpty()) {
            v.post.backgroundTintList = getColorStateList(R.color.colorPrimary)
            v.post.isEnabled = true
        } else {
            v.post.isEnabled = false
            v.post.backgroundTintList = getColorStateList(R.color.disabled)
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
        mime = contentResolver.getType(it).toString()
        v.imageView.setImageBitmap(bitmap)
        v.addImage.setColorFilter(getColor(R.color.disabled))
        v.imageContainer.visibility = View.VISIBLE
        checkIfCanPost()
    }

    private fun randomStringForHint() {
        v.editText.hint = getString(randomStrings[Random.nextInt(0, 4)])
    }

    private fun postEverything(postToReply: String?) {
        v.loading.visibility = View.VISIBLE
        val post = AllesInteractionPost(
            content = v.editText.text.toString(),
            image = if (bitmap != null) { "data:$mime;base64,${ImageUtils.convertToBase64(bitmap!!)}" } else { null },
            parent = postToReply
        )
        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        val call = retrofit.post(post)
        v.post.isEnabled = false
        v.cancel.setOnClickListener { call.cancel(); finish() }

        call.enqueue(object : Callback<AllesInteractionPost> {
            override fun onFailure(call: Call<AllesInteractionPost>, t: Throwable) {
                v.loading.visibility = View.GONE
                Snackbar.make(v.editText, R.string.post_snackbar_error, Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AllesInteractionPost>, response: Response<AllesInteractionPost>) {
                setResult(69)
                finish()
            }
        })
    }
}