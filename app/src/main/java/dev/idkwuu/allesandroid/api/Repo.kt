package dev.idkwuu.allesandroid.api

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.idkwuu.allesandroid.models.*
import dev.idkwuu.allesandroid.util.BookmarksManager
import dev.idkwuu.allesandroid.util.dont_care_lol
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repo {
    companion object {
        val retrofitInstance: AllesEndpointsInterface by lazy {
            RetrofitClientInstance().getRetrofitInstance().create(AllesEndpointsInterface::class.java)
        }

        val cachedEtags = ArrayList<Pair<String, String?>>()

        var shouldStopLoop = false
        val handler = Handler()
        var onlineRunnable: Runnable = object : Runnable {
            override fun run() {
                if (!shouldStopLoop) {
                    val call = retrofitInstance.online()
                    call.enqueue(dont_care_lol)
                    handler.postDelayed(this, 10000)
                }
            }
        }
    }

    fun getPosts(context: Context): LiveData<MutableList<AllesPost>?> {
        val mutableData = MutableLiveData<MutableList<AllesPost>?>()
        val call = retrofitInstance.getFeed()
        call.enqueue(object : BaseCallback<AllesFeed>(context) {
            override fun onFailure(call: Call<AllesFeed>?, t: Throwable?) {
                mutableData.value = null
            }

            override fun onSuccess(response: AllesFeed) {
                mutableData.value = response.feed.toMutableList()
            }
        })
        return mutableData
    }

    fun getUser(context: Context, user: String): LiveData<AllesUser?> {
        val mutableData = MutableLiveData<AllesUser?>()
        val call = retrofitInstance.getUser(user)
        call.enqueue(object : BaseCallback<AllesUser>(context) {
            override fun onFailure(call: Call<AllesUser>?, t: Throwable?) {
                mutableData.value = null
            }

            override fun onSuccess(response: AllesUser) {
                mutableData.value = response
            }
        })
        return mutableData
    }

    fun getUserPosts(context: Context, user: String): LiveData<MutableList<AllesPost>?> {
        val mutableData = MutableLiveData<MutableList<AllesPost>?>()
        val call = retrofitInstance.getUserPosts(user)
        call.enqueue(object : BaseCallback<AllesUser>(context) {
            override fun onFailure(call: Call<AllesUser>?, t: Throwable?) {
                mutableData.value = null
            }

            override fun onSuccess(response: AllesUser) {
                mutableData.value = response.posts.toMutableList()
            }
        })
        return mutableData
    }

    fun getIsOnline(user: String): LiveData<Boolean> {
        val isOnline = MutableLiveData<Boolean>()
        val call = retrofitInstance.getIsOnline(user, (System.currentTimeMillis() / 1000).toString())
        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isOnline.value = false
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    isOnline.value = response.body()!!.string() == "\uD83D\uDFE2"
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
                    val etagPair = Pair(user, response.headers()["etag"]!!)
                    cachedEtags.add(etagPair)
                    etag.value = etagPair
                }
            })
        }
        return etag
    }

    fun getMentions(context: Context): LiveData<MutableList<AllesPost>?> {
        val mutableData = MutableLiveData<MutableList<AllesPost>?>()
        val call = retrofitInstance.getMentions()
        call.enqueue(object : BaseCallback<AllesMentions>(context) {
            override fun onFailure(call: Call<AllesMentions>?, t: Throwable?) {
                mutableData.value = null
            }

            override fun onSuccess(response: AllesMentions) {
                mutableData.value = response.mentions.toMutableList()
            }
        })
        return mutableData
    }

    fun getPost(context: Context, slug: String): LiveData<AllesPost?> {
        val mutableData = MutableLiveData<AllesPost?>()
        val call = retrofitInstance.getThread(slug)
        call.enqueue(object : BaseCallback<AllesPost>(context) {
            override fun onFailure(call: Call<AllesPost>?, t: Throwable?) {
                mutableData.value = null
            }

            override fun onSuccess(response: AllesPost) {
                mutableData.value = response
            }
        })
        return mutableData
    }

    fun getBookmarks(context: Context): LiveData<MutableList<AllesPost>?> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        val bookmarks = BookmarksManager.data.value!!
        val list = mutableListOf<AllesPost>()
        repeat(bookmarks.size) {
            val call = retrofitInstance.getPost(bookmarks[it])
            call.enqueue(object : BaseCallback<AllesPost>(context) {
                override fun onFailure(call: Call<AllesPost>?, t: Throwable?) {
                    mutableData.value = null
                }

                override fun onSuccess(response: AllesPost) {
                    list.add(response)
                    if (it == bookmarks.size - 1) {
                        mutableData.value = list.toMutableList()
                    }
                }
            })
        }
        return mutableData
    }
}