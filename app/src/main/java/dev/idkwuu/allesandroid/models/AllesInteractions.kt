package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesInteractionPost(
    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("image")
    @Expose
    var image: String = ""
)

class AllesInteractionRemove {
    @SerializedName("post")
    @Expose
    var post: String = ""
}