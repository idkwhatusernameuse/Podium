package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LegacyUserCredentials {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

}

class LegacyToken {
    @SerializedName("token")
    @Expose
    var token: String? = null
}