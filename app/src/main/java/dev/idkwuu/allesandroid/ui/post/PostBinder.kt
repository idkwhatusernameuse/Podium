package dev.idkwuu.allesandroid.ui.post

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.models.AllesVote
import dev.idkwuu.allesandroid.ui.ImageViewerActivity
import dev.idkwuu.allesandroid.ui.profile.ProfileActivity
import dev.idkwuu.allesandroid.ui.ThreadActivity
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.TextClickableSpan
import dev.idkwuu.allesandroid.util.dont_care_lol
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.os.Handler
import dev.idkwuu.allesandroid.api.AllesEndpoints
import dev.idkwuu.allesandroid.models.AllesUser
import dev.idkwuu.allesandroid.util.BookmarksManager
import org.json.JSONObject

class PostBinder {
    private fun vote(itemView: View, slug: String, vote: Int, currentVote: Int) {
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

        Repo.retrofitInstance.vote(slug, AllesVote(vote)).enqueue(dont_care_lol)
    }

    @SuppressLint("SetTextI18n")
    fun bindView(post: AllesPost, itemView: View, isMainPost: Boolean = false) {
        itemView.user_info.setOnClickListener {
            val intent = Intent(itemView.context, ProfileActivity::class.java)
            intent.putExtra("user", post.author)
            itemView.context.startActivity(intent)
        }
        // Author name
        val author = Gson().fromJson(post.users[post.author].toString(), AllesUser::class.java)
        if (author.plus) {
            itemView.user_title.text = "${author.name}\u207A"
        } else {
            itemView.user_title.text = author.name
        }
        // Votes
        itemView.votesCount.text = post.vote.score.toString()
        var actualVote = post.vote.me
        itemView.plus.setOnClickListener {
            actualVote = if (actualVote == 1) {
                vote(itemView, post.id, 0, actualVote)
                0
            } else {
                vote(itemView, post.id, 1, actualVote)
                1
            }
        }
        itemView.minus.setOnClickListener {
            actualVote = if (actualVote == -1) {
                vote(itemView, post.id, 0, actualVote)
                0
            } else {
                vote(itemView, post.id, -1, actualVote)
                -1
            }
        }
        // Comments
        itemView.comments_count.text = post.children.count.toString()
        if (post.children.count > 0) {
            itemView.comments_icon.setImageResource(R.drawable.ic_fluent_chat_20_filled)
        }
        // Post views
        if (post.interactions != null) {
            itemView.views.visibility = View.VISIBLE
            itemView.views_text.text = post.interactions.toString()
        }
        // Has user voted?
        if (post.vote.me == 1) ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(
            ContextCompat.getColor(itemView.context, R.color.plus_selected)))
        else if (post.vote.me == -1) ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(
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
            val imageURL = "${AllesEndpoints.fs}${post.image}"
            Glide.with(itemView.context.applicationContext).load(imageURL).into(itemView.post_image)
            itemView.post_image.setOnClickListener {
                val intent = Intent(itemView.context, ImageViewerActivity::class.java)
                intent.putExtra("URL", imageURL)
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
        val pfpURL = "${AllesEndpoints.fs}${post.author}?size=40"
        Repo().getEtagProfilePicture(post.author).observeForever { etag ->
            if (etag?.second != null) {
                Glide.with(itemView.context.applicationContext)
                    .load(pfpURL)
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
        // Popup menu
        itemView.options.setOnClickListener {
            openPopup(
                it,
                post.id,
                post.author,
                if (post.author == SharedPreferences.current_user) itemView else null
            )
        }
    }

    private fun openPopup(view: View, slug: String, username: String, parentView: View? = null) {
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupWindow = PopupWindow(
            inflater.inflate(R.layout.layout_popup_post, RelativeLayout(view.context), false),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        // Copy link
        popupWindow.contentView.findViewById<TextView>(R.id.copy).setOnClickListener {
            val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Alles Post", "https://alles.cx/$username/$slug")
            clipboard.setPrimaryClip(clip)
            Snackbar.make(view, R.string.copied, Snackbar.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }
        // Delete post
        if (parentView != null) {
            val delete = popupWindow.contentView.findViewById<TextView>(R.id.delete)
            delete.visibility = View.VISIBLE
            delete.setOnClickListener {
                // aNiMaTiOnS!!!1!1!1!1
                animateViewHeight(parentView.removed, ViewGroup.LayoutParams.MATCH_PARENT)
                val handler = Handler()
                handler.postDelayed(
                    deleteRunnable(slug, parentView),
                    3000
                )
                popupWindow.dismiss()

                // Confirmation
                parentView.removed.setOnClickListener {
                    Repo.retrofitInstance.remove(slug).enqueue(dont_care_lol)
                    animateViewHeight(parentView.post, 0)
                    parentView.removed_text.text = parentView.context.getString(R.string.removed)
                    deleteConfirmation = true
                }
            }
        }
        // Save post
        val save = popupWindow.contentView.findViewById<TextView>(R.id.save)
        if (BookmarksManager.data.value?.contains(slug)!!){
            save.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_fluent_bookmark_24_filled,
                0,
                0,
                0
            )
            save.text = view.context.getString(R.string.bookmark_post_remove)

            save.setOnClickListener {
                BookmarksManager().remove(slug)
                Snackbar.make(view, R.string.removed_from_bookmarks, Snackbar.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }
        } else {
            save.setOnClickListener {
                BookmarksManager().save(slug)
                Snackbar.make(view, R.string.added_to_bookmarks, Snackbar.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }
        }

        popupWindow.showAsDropDown(view)
    }

    private var deleteConfirmation = false

    private fun deleteRunnable(slug: String, parentView: View): Runnable = Runnable {
        if (!deleteConfirmation) {
            animateViewHeight(parentView.removed, 0)
        }
    }

    private fun animateViewHeight(view: ViewGroup, height: Int) {
        val transition = ChangeBounds()
        transition.interpolator = DecelerateInterpolator()
        TransitionManager.beginDelayedTransition(view, transition)
        val paramsRoot = view.layoutParams
        paramsRoot.height = height
        view.layoutParams = paramsRoot
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