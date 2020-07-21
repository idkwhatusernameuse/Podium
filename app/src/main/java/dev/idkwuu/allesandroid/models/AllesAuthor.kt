package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesAuthor (
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("plus")
    @Expose
    var plus: Boolean
)