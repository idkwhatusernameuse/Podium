package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        val json = intent.getStringExtra("json")
        if (json == null) {
            slug = intent.getStringExtra("post")!!
            load()
        } else {
            val post = Gson().fromJson(json, AllesPost::class.java)
            slug = post.slug
            load(post)
        }

        // Back button
        findViewById<ImageButton>(R.id.back).setOnClickListener { finish() }
    }

    private fun load(post: AllesPost? = null) {
        if (post != null) {
            PostBinder().bindView(post, findViewById(R.id.main_post), true)
        }
        Repo().getPost(this, slug).observeForever {
            val errorLayout = findViewById<View>(R.id.error_loading)
            if (it != null) {
                errorLayout.visibility = View.GONE
                if (post == null) {
                    PostBinder().bindView(it, findViewById(R.id.main_post), true)
                }
                setThread(it)
            } else {
                errorLayout.visibility = View.VISIBLE
                findViewById<Button>(R.id.reply).visibility = View.GONE
                errorLayout.findViewById<Button>(R.id.retry).setOnClickListener {
                    errorLayout.visibility = View.GONE
                    load(post)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setThread(post: AllesPost) {
        // Reply button
        val replyButton = findViewById<Button>(R.id.reply)
        replyButton.text = "${getString(R.string.reply)} @${post.author.username}"
        replyButton.visibility = View.VISIBLE
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
            load()
        }
    }
}