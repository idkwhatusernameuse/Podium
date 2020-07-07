package dev.idkwuu.allesandroid.util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val dont_care_lol = object : Callback<Void> {
    override fun onFailure(call: Call<Void>, t: Throwable) {}
    override fun onResponse(call: Call<Void>, response: Response<Void>) {}
}