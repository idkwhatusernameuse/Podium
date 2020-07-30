package dev.idkwuu.allesandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class License (
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("file")
    @Expose
    val file: String
)

class LicensesList(
    @SerializedName("licenses")
    @Expose
    val list: List<License>
)