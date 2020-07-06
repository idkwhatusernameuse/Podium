package dev.idkwuu.allesandroid.ui.feed

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.AllesPost
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class FeedAdapter(
    private val context: Context
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var dataList = mutableListOf<AllesPost>()

    fun setListData(data: MutableList<AllesPost>) {
        dataList = data
    }

    inner class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(post: AllesPost) {
            // Author name
            if (post.author?.plus!!) {
                itemView.user_title.text = "${post.author?.name}\u207A"
            } else {
                itemView.user_title.text = post.author?.name
            }
            // Author username
            itemView.user_handle.text = "@${post.author?.username}"
            // Votes
            itemView.votesCount.text = post.score.toString()
            // Comments
            itemView.comments_count.text = post.replyCount.toString()
            if (post.replyCount!! > 0) {
                itemView.comments_icon.setImageResource(R.drawable.ic_fluent_chat_20_filled)
            }
            // Has user voted?
            if (post.vote == 1) ImageViewCompat.setImageTintList(itemView.plus, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.plus_selected)))
            else if (post.vote == -1) ImageViewCompat.setImageTintList(itemView.minus, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.minus_selected)))
            // Set post content
            if (post.content!!.isNotEmpty()) {
                itemView.post_text.visibility = View.VISIBLE
                itemView.post_text.text = post.content
            }
            // Post image
            if (post.image != null) {
                itemView.post_image.visibility = View.VISIBLE
                Glide.with(context).load(post.image).into(itemView.post_image)
            }
            // Set post longevity
            /*val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            TimeZone.setDefault(null)
            sdf.timeZone = TimeZone.getDefault()
            val time = sdf.parse(post.createdAt!!)!!.time
            val now = System.currentTimeMillis()
            itemView.time.text = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)*/
            itemView.time.text = post.createdAt
            // Set profile photo
            Glide.with(context).load("https://avatar.alles.cx/u/${post.author?.username}?size=100").into(itemView.profile_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = dataList[position]
        holder.bindView(post)
    }
}