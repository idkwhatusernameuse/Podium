package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesPost {
    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

    @SerializedName("author")
    @Expose
    var author: AllesAuthor? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("image")
    @Expose
    var image: Any? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("score")
    @Expose
    var score: Int? = null

    @SerializedName("vote")
    @Expose
    var vote: Int? = null

    @SerializedName("replyCount")
    @Expose
    var replyCount: Int? = null

    @SerializedName("hasParent")
    @Expose
    var hasParent: Boolean? = null
}

