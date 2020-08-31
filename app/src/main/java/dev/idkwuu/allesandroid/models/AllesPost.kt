package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class AllesPost(
    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("parent")
    val parent: String?,

    @SerializedName("children")
    val children: Children,

    @SerializedName("content")
    @Expose
    val content: String,

    @SerializedName("image")
    @Expose
    val image: String? = null,

    @SerializedName("url")
    @Expose
    val url: String? = null,

    @SerializedName("vote")
    val vote: Vote,

    @SerializedName("interactions")
    val interactions: Int?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("users")
    val users: JSONObject
)

class Children(
    @SerializedName("count")
    val count: Int,

    @SerializedName("list")
    val list: List<String>
)

class Vote(
    @SerializedName("score")
    val score: Int,

    @SerializedName("me")
    val me: Int
)