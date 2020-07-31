package dev.idkwuu.allesandroid.ui.post

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.gson.Gson
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.models.AllesVote
import dev.idkwuu.allesandroid.ui.ImageViewerActivity
import dev.idkwuu.allesandroid.ui.ProfileActivity
import dev.idkwuu.allesandroid.ui.ThreadActivity
import dev.idkwuu.allesandroid.util.TextClickableSpan
import dev.idkwuu.allesandroid.util.dont_care_lol
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostBinder {
    private fun vote(itemView: View, slug: String, vote: Int, currentVote: Int) {
        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        when (vote) {
            0 -> {
                ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.neutral)))
                ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.neutral)))
                itemView.votesCount.text =
                    (itemView.votesCount.text.toString().toInt() - currentVote).toString()
            }
            1 -> {
                ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.plus_selected)))
                ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.neutral)))
                itemView.votesCount.text = if (currentVote == -1) {
                    (itemView.votesCount.text.toString().toInt() + 2).toString()
                } else {
                    (itemView.votesCount.text.toString().toInt() + 1).toString()
                }
            }
            -1 -> {
                ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.neutral)))
                ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.minus_selected)))
                itemView.votesCount.text = if (currentVote == 1) {
                    (itemView.votesCount.text.toString().toInt() - 2).toString()
                } else {
                    (itemView.votesCount.text.toString().toInt() - 1).toString()
                }
            }
        }

        retrofit.vote(slug, AllesVote(vote)).enqueue(dont_care_lol)
    }

    @SuppressLint("SetTextI18n")
    fun bindView(post: AllesPost, itemView: View, isMainPost: Boolean = false) {
        itemView.user_info.setOnClickListener {
            val intent = Intent(itemView.context, ProfileActivity::class.java)
            intent.putExtra("user", post.author.username)
            itemView.context.startActivity(intent)
        }
        // Author name
        if (post.author.plus) {
            itemView.user_title.text = "${post.author.name}\u207A"
        } else {
            itemView.user_title.text = post.author.name
        }
        // Author username
        itemView.user_handle.text = "@${post.author.username}"
        // Votes
        itemView.votesCount.text = post.score.toString()
        var actualVote = post.vote
        itemView.plusInternal.setOnClickListener {
            actualVote = if (actualVote == 1) {
                vote(itemView, post.slug, 0, actualVote)
                0
            } else {
                vote(itemView, post.slug, 1, actualVote)
                1
            }
        }
        itemView.minusInternal.setOnClickListener {
            actualVote = if (actualVote == -1) {
                vote(itemView, post.slug, 0, actualVote)
                0
            } else {
                vote(itemView, post.slug, -1, actualVote)
                -1
            }
        }
        // Comments
        itemView.comments_count.text = post.replyCount.toString()
        if (post.replyCount > 0) {
            itemView.comments_icon.setImageResource(R.drawable.ic_fluent_chat_20_filled)
        }
        // Has user voted?
        if (post.vote == 1) ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(
            ContextCompat.getColor(itemView.context, R.color.plus_selected)))
        else if (post.vote == -1) ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(
            ContextCompat.getColor(itemView.context, R.color.minus_selected)))
        // Set post content
        if (post.content.isNotEmpty()) {
            itemView.post_text.visibility = View.VISIBLE
            itemView.post_text.text = setClickableHashtagsUsernames(post.content)
            itemView.post_text.movementMethod = LinkMovementMethod.getInstance()
        }
        // Post image
        if (post.image != null) {
            itemView.card_image.visibility = View.VISIBLE
            Glide.with(itemView.context.applicationContext).load(post.image).into(itemView.post_image)
            itemView.post_image.setOnClickListener {
                val intent = Intent(itemView.context, ImageViewerActivity::class.java)
                intent.putExtra("URL", post.image.toString())
                itemView.context.startActivity(intent)
            }
        }
        // Set post longevity
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val timeAndDate = sdf.parse(post.createdAt)
        val now = System.currentTimeMillis()
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeAndDate!!)
        itemView.time.text = "${DateUtils.getRelativeTimeSpanString(timeAndDate.time, now, 0)}, $time"
        // Set profile photo
        Repo().getEtagProfilePicture(post.author.username).observeForever { etag ->
            if (etag?.second != null) {
                Glide.with(itemView.context.applicationContext)
                    .load("https://avatar.alles.cx/u/${post.author.username}")
                    .signature(ObjectKey(etag.second!!))
                    .into(itemView.profile_image)
            }
        }
        if (!isMainPost) {
            // Open thread
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ThreadActivity::class.java)
                intent.putExtra("json", Gson().toJson(post))
                itemView.context.startActivity(intent)
            }
        }
    }

    // Enjoy this long function name c:
    private fun checkIfCharShouldBeClickable(char: Char): Boolean =
        char == '#' || char == '%' || char == '@'

    private fun createSpannableString(word: String): SpannableString {
        val string = SpannableString(word)
        string.setSpan(TextClickableSpan(word), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(Color.BLUE, 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return string
    }

    private fun setClickableHashtagsUsernames(text: String): SpannableString {
        val finalString = SpannableStringBuilder()
        val lineSplit = text.split("\n")

        // Go through every line
        repeat(lineSplit.size) { i ->
            // Add a line break because the last split got rid of all of them
            if (i > 0 && i < lineSplit.size) {
                finalString.append("\n")
            }
            // Split each line into an array of words
            val textSplit = lineSplit[i].split(" ")
            // Go through every word
            repeat(textSplit.size) { j ->
                // Add a space
                if (j > 0 && j < textSplit.size) {
                    finalString.append(" ")
                }
                // Split the word if the #, @ and/or & are together (wtf?)
                val wordSplit = textSplit[j].split(Regex("(?=#)|(?=%)|(?=@)"))
                repeat(wordSplit.size){ k ->
                    finalString.append(
                        // Check if the string isn't empty, otherwise, KotlinNullPointerException c:
                        if (wordSplit[k].isNotEmpty()) {
                            // Check the first character of the word
                            if (checkIfCharShouldBeClickable(wordSplit[k][0])) {
                                // Set hashtag/username/post
                                SpannableString(createSpannableString(wordSplit[k]))
                            } else {
                                // Add the word
                                wordSplit[k]
                            }
                        } else {
                            wordSplit[k]
                        }
                    )
                }
            }
        }

        return SpannableString(finalString)
    }
}