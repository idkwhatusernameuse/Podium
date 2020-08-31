package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesUser (
    @SerializedName("id")
    val id: String?,

    @SerializedName("alles")
    val alles: Boolean,

    @SerializedName("name")
    val name: String,

    @SerializedName("tag")
    val tag: String?,

    @SerializedName("plus")
    val plus: Boolean,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("avatar")
    val avatar: Any?, // TODO

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("xp")
    val xp: Any?, // TODO

    @SerializedName("posts")
    val posts: Any?, // TODO

    @SerializedName("followers")
    val followers: Any?, // TODO

    @SerializedName("following")
    val following: Any?, // TODO

    @SerializedName("labels")
    val labels: List<Any>? // TODO
)