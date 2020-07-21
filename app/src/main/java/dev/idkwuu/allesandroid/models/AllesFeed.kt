package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesFeed (
    @SerializedName("feed")
    @Expose
    var feed: List<AllesPost>
)
