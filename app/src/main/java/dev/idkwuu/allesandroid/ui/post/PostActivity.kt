package dev.idkwuu.allesandroid.ui.post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.models.AllesInteractionPost
import dev.idkwuu.allesandroid.util.ImageUtil
import dev.idkwuu.allesandroid.util.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PostActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_post)

        val addImage = findViewById<ImageButton>(R.id.addImage)
        val post = findViewById<Button>(R.id.post)
        val editText = findViewById<EditText>(R.id.editText)

        // Replying to
        val postToReply = intent.getStringExtra("replyTo")
        if (postToReply != null) {
            findViewById<TextView>(R.id.replyingTo).text = "${getString(R.string.replying)} @${intent.getStringExtra("userToReply")}"
        }

        // Get a random phrase for the hint
        randomStringForHint()

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIfCanPost()
                if (count == 0) randomStringForHint()
            }
        })

        addImage.setOnClickListener {
            if (bitmap == null) loadImage()
        }

        post.setOnClickListener {
            postEverything(postToReply)
        }

        // Back button
        findViewById<ImageButton>(R.id.back).setOnClickListener { finish() }

        // Remove photo button
        findViewById<ImageButton>(R.id.removePhoto).setOnClickListener {
            bitmap = null
            mime = ""
            addImage.setColorFilter(getColor(R.color.neutral))
            findViewById<ConstraintLayout>(R.id.imageContainer).visibility = View.GONE
            checkIfCanPost()
        }
    }

    private fun checkIfCanPost() {
        val post = findViewById<Button>(R.id.post)
        if (findViewById<EditText>(R.id.editText).text.toString().isNotEmpty()) {
            post.backgroundTintList = getColorStateList(R.color.colorPrimary)
            post.isEnabled = true
        } else {
            post.isEnabled = false
            post.backgroundTintList = getColorStateList(R.color.disabled)
        }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val inputStream = data?.data?.let { applicationContext.contentResolver.openInputStream(it) }!!
            bitmap = BitmapFactory.decodeStream(inputStream)
            mime = contentResolver.getType(data.data!!).toString()
            findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            findViewById<ImageButton>(R.id.addImage).setColorFilter(getColor(R.color.disabled))
            findViewById<ConstraintLayout>(R.id.imageContainer).visibility = View.VISIBLE
            inputStream.close()
            checkIfCanPost()
        }
    }

    private fun randomStringForHint() {
        findViewById<EditText>(R.id.editText).hint = getString(randomStrings[Random.nextInt(0, 4)])
    }

    private fun postEverything(postToReply: String?) {
        val loading = findViewById<LinearLayout>(R.id.loading)
        loading.visibility = View.VISIBLE
        val post = AllesInteractionPost(
            content = findViewById<EditText>(R.id.editText).text.toString(),
            image = if (bitmap != null) { "data:$mime;base64,${ImageUtil.convertToBase64(bitmap!!)}" } else { null },
            parent = postToReply
        )
        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        val call = retrofit.post(post)

        findViewById<Button>(R.id.cancel).setOnClickListener { call.cancel(); finish() }

        call.enqueue(object : Callback<AllesInteractionPost> {
            override fun onFailure(call: Call<AllesInteractionPost>, t: Throwable) {
                loading.visibility = View.GONE
                Snackbar.make(findViewById(R.id.editText), R.string.post_snackbar_error, Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AllesInteractionPost>, response: Response<AllesInteractionPost>) {
                setResult(69)
                finish()
            }
        })
    }
}