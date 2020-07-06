package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesVote(
    @SerializedName("vote")
    @Expose
    val vote: Int? = 0
)