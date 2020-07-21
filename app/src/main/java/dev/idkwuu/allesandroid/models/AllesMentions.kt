package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesMentions (
    @SerializedName("mentions")
    @Expose
    var mentions: List<AllesPost>
)

