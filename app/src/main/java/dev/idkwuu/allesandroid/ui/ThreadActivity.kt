package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.ui.post.PostActivity
import dev.idkwuu.allesandroid.ui.post.PostBinder
import dev.idkwuu.allesandroid.ui.post.PostListAdapter

class ThreadActivity : AppCompatActivity() {

    private var slug: String = ""
    private lateinit var gPost: AllesPost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        val json = intent.getStringExtra("json")
        if (json == null) {
            slug = intent.getStringExtra("post")!!
            loadFromServer()
        } else {
            gPost = Gson().fromJson(json, AllesPost::class.java)
            slug = gPost.slug
            load(gPost)
        }

        // Back button
        findViewById<ImageButton>(R.id.back).setOnClickListener { finish() }
    }

    private fun loadFromServer() {
        Repo().getPost(slug).observeForever {
            gPost = it
            load(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun load(post: AllesPost) {
        PostBinder().bindView(post, findViewById(R.id.main_post), true)

        // Reply button
        val replyButton = findViewById<Button>(R.id.reply)
        replyButton.text = "${getString(R.string.reply)} @${post.author.username}"
        replyButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("replyTo", post.slug)
            intent.putExtra("userToReply", post.author.username)
            startActivityForResult(intent, 69)
        }

        // Get post replies and ancestors
        setAncestorsAndReplies(post.ancestors, post.replies)
    }

    private fun setAncestorsAndReplies(ancestors: List<AllesPost>?, replies: List<AllesPost>?) {
        // Ancestors recyclerView
        if (ancestors != null) {
            val ancestorsRV = findViewById<RecyclerView>(R.id.recyclerView_ancestors)
            val ancestorsAdapter = PostListAdapter(this)
            ancestorsRV.layoutManager = LinearLayoutManager(this)
            ancestorsRV.adapter = ancestorsAdapter
            ancestorsRV.isNestedScrollingEnabled = false
            ancestorsAdapter.setListData(ancestors)
        }

        // Replies recyclerView
        if (replies != null) {
            val repliesRV = findViewById<RecyclerView>(R.id.recyclerView_replies)
            val repliesAdapter = PostListAdapter(this)
            repliesRV.layoutManager = LinearLayoutManager(this)
            repliesRV.adapter = repliesAdapter
            repliesRV.isNestedScrollingEnabled = false
            repliesAdapter.setListData(replies)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 69) {
            loadFromServer()
        }
    }
}