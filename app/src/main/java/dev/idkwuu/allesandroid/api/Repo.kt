package dev.idkwuu.allesandroid.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dev.idkwuu.allesandroid.models.*
import dev.idkwuu.allesandroid.util.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repo {
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
                    mutableData.value = response.body()!!.feed!!.toMutableList()
                }
            }
        })

        return mutableData
    }

    fun getUser(user: String): LiveData<AllesUser> {
        val mutableData = MutableLiveData<AllesUser>()

        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        val call = retrofit.getUser(SharedPreferences.login_token!!, user)


        call.enqueue(object : Callback<AllesUser> {
            override fun onFailure(call: Call<AllesUser>, t: Throwable) {
                Log.d("Retrofit", t.message!!)
            }

            override fun onResponse(call: Call<AllesUser>, response: Response<AllesUser>) {
                if (response.body() != null) {
                    mutableData.value = response.body()!!
                }
            }
        })

        return mutableData
    }
}