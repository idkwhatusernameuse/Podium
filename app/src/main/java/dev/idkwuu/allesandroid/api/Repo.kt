package dev.idkwuu.allesandroid.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.idkwuu.allesandroid.models.*
import dev.idkwuu.allesandroid.util.SharedPreferences
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class Repo {
    companion object {
        private lateinit var instance: AllesEndpointsInterface

        val retrofitInstance: AllesEndpointsInterface
            get() {
                if (!this::instance.isInitialized) {
                    instance = RetrofitClientInstance().getRetrofitInstance()
                        .create(AllesEndpointsInterface::class.java)
                }
                return instance
            }
    }

    fun getPosts(): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        val call = retrofitInstance.getFeed(SharedPreferences.login_token!!)
        call.enqueue(object : Callback<AllesFeed> {
            override fun onFailure(call: Call<AllesFeed>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<AllesFeed>, response: Response<AllesFeed>) {
                if (response.body()?.feed != null) {
                    mutableData.value = response.body()!!.feed.toMutableList()
                }
            }
        })
        return mutableData
    }

    fun getUser(user: String): LiveData<AllesUser> {
        val mutableData = MutableLiveData<AllesUser>()
        val call = retrofitInstance.getUser(SharedPreferences.login_token!!, user)
        call.enqueue(object : Callback<AllesUser> {
            override fun onFailure(call: Call<AllesUser>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<AllesUser>, response: Response<AllesUser>) {
                if (response.body() != null) {
                    mutableData.value = response.body()!!
                }
            }
        })
        return mutableData
    }

    // Not working currently. For some reason it skips directly to the return instead of doing the
    // request
    fun getIsOnline(user: String): Boolean {
        var isOnline = false
        val call = retrofitInstance.getIsOnline(user)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body() != null) {
                    isOnline = response.body()!! == "\uD83D\uDFE2"
                }
            }
        })
        return isOnline
    }

    fun getEtagProfilePicture(user: String): LiveData<String> {
        val etag = MutableLiveData<String>()
        val call = retrofitInstance.getImageHeaders(user)
        call.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.headers() != null) {
                    etag.value = response.headers()!!["etag"]!!
                }
            }

        })
        return etag
    }
}