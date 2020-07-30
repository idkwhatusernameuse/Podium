package dev.idkwuu.allesandroid.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.idkwuu.allesandroid.models.*
import dev.idkwuu.allesandroid.ui.MainActivity
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

        val cachedEtags = ArrayList<Pair<String, String?>>()
        var cachedFeed: List<AllesPost>? = null
    }

    fun getPosts(reload: Boolean = false): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        if (cachedFeed == null || reload) {
            val call = retrofitInstance.getFeed(SharedPreferences.login_token!!)
            call.enqueue(object : Callback<AllesFeed> {
                override fun onFailure(call: Call<AllesFeed>, t: Throwable) {

                }
                override fun onResponse(call: Call<AllesFeed>, response: Response<AllesFeed>) {
                    if (response.body()?.feed != null) {
                        cachedFeed = response.body()!!.feed
                        mutableData.value = response.body()!!.feed.toMutableList()
                    }
                }
            })
        } else {
            mutableData.value = cachedFeed!!.toMutableList()
        }
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

    fun getEtagProfilePicture(user: String): LiveData<Pair<String, String?>?> {
        val data = cachedEtags.find { it.first == user }
        val etag = MutableLiveData(data)
        // Check if we already have cached an etag
        if (data == null) {
            val call = retrofitInstance.getImageHeaders(user)
            call.enqueue(object: Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) { }
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.headers() != null) {
                        val etagPair = Pair(user, response.headers()!!["etag"]!!)
                        cachedEtags.add(etagPair)
                        etag.value = etagPair
                    }
                }
            })
        }
        return etag
    }

    fun getMentions(): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        val call = retrofitInstance.getMentions(SharedPreferences.login_token!!)
        call.enqueue(object : Callback<AllesMentions> {
            override fun onFailure(call: Call<AllesMentions>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<AllesMentions>, response: Response<AllesMentions>) {
                if (response.body()?.mentions != null) {
                    mutableData.value = response.body()!!.mentions.toMutableList()
                }
            }
        })
        return mutableData
    }

    fun getPost(slug: String): LiveData<AllesPost> {
        val mutableData = MutableLiveData<AllesPost>()
        val call = retrofitInstance.getPost(SharedPreferences.login_token!!, slug)
        call.enqueue(object : Callback<AllesPost> {
            override fun onFailure(call: Call<AllesPost>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<AllesPost>, response: Response<AllesPost>) {
                if (response.body() != null) {
                    mutableData.value = response.body()!!
                }
            }
        })
        return mutableData
    }
}