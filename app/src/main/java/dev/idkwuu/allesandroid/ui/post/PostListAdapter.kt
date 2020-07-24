package dev.idkwuu.allesandroid.ui.post

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.AllesPost

class PostListAdapter(
    private val context: Context
) : RecyclerView.Adapter<PostListAdapter.FeedViewHolder>() {

    private var dataList = listOf<AllesPost>()

    fun setListData(data: List<AllesPost>) {
        dataList = data
    }

    inner class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(post: AllesPost) {
            PostBinder().bindView(post, itemView)
        }
    }

    private val viewTypes = listOf(R.layout.item_post)

    override fun onCreateViewHolder(parent: ViewGroup, id: Int): FeedViewHolder {
        val view = LayoutInflater.from(context).inflate(viewTypes[id], parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = dataList[position]
        holder.bindView(post)
    }
}