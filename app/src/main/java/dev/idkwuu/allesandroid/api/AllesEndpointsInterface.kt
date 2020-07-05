package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.models.AllesFeed
import dev.idkwuu.allesandroid.models.LegacyToken
import dev.idkwuu.allesandroid.models.LegacyUserCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AllesEndpointsInterface {

    @POST("login")
    fun getToken(@Body credentials: LegacyUserCredentials): Call<LegacyToken?>?

    @GET("feed")
    fun getFeed(@Header("authorization") token: String): Call<AllesFeed?>?

}