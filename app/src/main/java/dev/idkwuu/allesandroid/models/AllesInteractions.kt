package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesInteractionPost(
    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null,

    @SerializedName("parent")
    @Expose
    var parent: String? = null
)

class AllesInteractionRemove {
    @SerializedName("post")
    @Expose
    var post: String = ""
}