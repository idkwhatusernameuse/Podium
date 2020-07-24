package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesPost(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("slug")
    @Expose
    var slug: String,

    @SerializedName("author")
    @Expose
    var author: AllesAuthor,

    @SerializedName("content")
    @Expose
    var content: String,

    @SerializedName("image")
    @Expose
    var image: String? = null,

    @SerializedName("createdAt")
    @Expose
    var createdAt: String,

    @SerializedName("score")
    @Expose
    var score: Int,

    @SerializedName("vote")
    @Expose
    var vote: Int,

    @SerializedName("replyCount")
    @Expose
    var replyCount: Int,

    @SerializedName("replies")
    @Expose
    var replies: List<AllesPost>?,

    @SerializedName("ancestors")
    @Expose
    var ancestors: List<AllesPost>?
)
