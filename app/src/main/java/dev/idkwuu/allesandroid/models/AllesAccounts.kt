package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllesAccounts {
    @SerializedName("primary")
    @Expose
    var primary: Account? = null

    @SerializedName("secondaries")
    @Expose
    var secondaries: List<Account>? = null

}

class Account {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("plus")
    @Expose
    var plus: Boolean? = null

}