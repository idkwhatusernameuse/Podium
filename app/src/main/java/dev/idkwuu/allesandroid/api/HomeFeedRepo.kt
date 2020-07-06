package dev.idkwuu.allesandroid.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dev.idkwuu.allesandroid.models.AllesFeed
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.util.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFeedRepo {
    fun getPosts(): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()

        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        val call = retrofit.getFeed(SharedPreferences.login_token!!)

        call.enqueue(object : Callback<AllesFeed> {
            override fun onFailure(call: Call<AllesFeed>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<AllesFeed>, response: Response<AllesFeed>) {
                if (response.body()?.feed != null) {
                    SharedPreferences.cached_feed = Gson().toJson(response.body())
                    mutableData.value = response.body()!!.feed!!.toMutableList()
                }
            }
        })

        return mutableData
    }
}