package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.models.*
import retrofit2.Call
import retrofit2.http.*

interface AllesEndpointsInterface {

    @POST("login")
    fun getToken(@Body credentials: LegacyUserCredentials): Call<LegacyToken>

    @GET("feed")
    fun getFeed(@Header("authorization") token: String): Call<AllesFeed>

    @GET("users/{username}")
    fun getUser(@Header("authorization") token: String, @Path("username") username: String): Call<AllesUser>

    @POST("post/{post}/vote")
    fun vote(@Header("authorization") token: String, @Path("post") post: String, @Body vote: Int): Call<AllesVote>

}