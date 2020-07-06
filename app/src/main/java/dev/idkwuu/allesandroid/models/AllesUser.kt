package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesUser {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("about")
    @Expose
    var about: String? = null

    @SerializedName("private")
    @Expose
    var private: Boolean? = null

    @SerializedName("followers")
    @Expose
    var followers: Int? = null

    @SerializedName("following")
    @Expose
    var following: Boolean? = null

    @SerializedName("followingUser")
    @Expose
    var followingUser: Boolean? = null

    @SerializedName("joinDate")
    @Expose
    var joinDate: String? = null

    @SerializedName("rubies")
    @Expose
    var rubies: Int? = null

    @SerializedName("plus")
    @Expose
    var plus: Boolean? = null

    @SerializedName("posts")
    @Expose
    var posts: List<AllesPost>? = null

    @SerializedName("color")
    @Expose
    var color: Any? = null

}