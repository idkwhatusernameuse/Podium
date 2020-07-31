package dev.idkwuu.allesandroid.api

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import dev.idkwuu.allesandroid.ui.SplashScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseCallback<T>(private var context: Context) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.code() == 401) {
            Repo.shouldStopLoop = true
            val intent = Intent(context, SplashScreen::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("logout", true)
            context.startActivity(intent)
            Runtime.getRuntime().exit(0)
        } else if (response.code() == 200){
            onSuccess(response.body()!!)
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {}

    abstract fun onSuccess(response: T)

}