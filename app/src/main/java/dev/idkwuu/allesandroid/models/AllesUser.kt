package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesUser (
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("nickname")
    @Expose
    var nickname: String,

    @SerializedName("about")
    @Expose
    var about: String? = null,

    @SerializedName("private")
    @Expose
    var private: Boolean,

    @SerializedName("followers")
    @Expose
    var followers: Int,

    @SerializedName("following")
    @Expose
    var following: Boolean,

    @SerializedName("followingUser")
    @Expose
    var followingUser: Boolean,

    @SerializedName("joinDate")
    @Expose
    var joinDate: String,

    @SerializedName("rubies")
    @Expose
    var rubies: Int,

    @SerializedName("plus")
    @Expose
    var plus: Boolean,

    @SerializedName("posts")
    @Expose
    var posts: List<AllesPost>? = null,

    @SerializedName("color")
    @Expose
    var color: Any? = null
)