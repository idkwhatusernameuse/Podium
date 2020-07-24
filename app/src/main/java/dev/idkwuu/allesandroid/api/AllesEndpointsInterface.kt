package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AllesEndpointsInterface {
    @POST("login")
    fun getToken(@Body credentials: LegacyUserCredentials): Call<LegacyToken>

    @GET("feed")
    fun getFeed(@Header("authorization") token: String): Call<AllesFeed>

    @GET("users/{username}?posts=")
    fun getUser(@Header("authorization") token: String, @Path("username") username: String): Call<AllesUser>

    @GET("users/{username}/follow")
    fun follow(@Header("authorization") token: String, @Path("username") username: String): Call<Void>

    @GET("users/{username}/unfollow")
    fun unfollow(@Header("authorization") token: String, @Path("username") username: String): Call<Void>

    @POST("post/{post}/vote")
    fun vote(@Header("authorization") token: String, @Path("post") post: String, @Body vote: AllesVote): Call<Void>

    @GET("mentions")
    fun getMentions(@Header("authorization") token: String): Call<AllesMentions>

    @GET("accounts")
    fun getAccounts(@Header("authorization") token: String): Call<AllesAccounts>

    @POST("post")
    fun post(@Header("authorization") token: String, @Body content: AllesInteractionPost): Call<AllesInteractionPost>

    @GET("post/{post}/remove")
    fun remove(@Header("authorization") token: String, @Path("post") post: String): Call<AllesInteractionRemove>

    @GET("https://online.alles.cx/{id}")
    fun getIsOnline(@Path("id") userId: String): Call<String>

    @HEAD("https://avatar.alles.cx/u/{username}")
    fun getImageHeaders(@Path("username") username: String): Call<Void>

    @GET("post/{slug}?children?=")
    fun getPost(@Header("authorization") token: String, @Path("slug") slug: String): Call<AllesPost>
}