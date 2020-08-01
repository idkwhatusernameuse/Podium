package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AllesEndpointsInterface {
    @POST("login")
    fun getToken(@Body credentials: LegacyUserCredentials): Call<LegacyToken>

    @GET("feed")
    fun getFeed(): Call<AllesFeed>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<AllesUser>

    @GET("users/{username}?posts=")
    fun getUserPosts(@Path("username") username: String): Call<AllesUser>

    @GET("users/{username}/follow")
    fun follow(@Path("username") username: String): Call<Void>

    @GET("users/{username}/unfollow")
    fun unfollow(@Path("username") username: String): Call<Void>

    @POST("post/{post}/vote")
    fun vote(@Path("post") post: String, @Body vote: AllesVote): Call<Void>

    @GET("mentions")
    fun getMentions(): Call<AllesMentions>

    @GET("accounts")
    fun getAccounts(): Call<AllesAccounts>

    @POST("post")
    fun post(@Body content: AllesInteractionPost): Call<AllesInteractionPost>

    @GET("post/{post}/remove")
    fun remove(@Path("post") post: String): Call<Void>

    @GET("https://online.alles.cx/{id}")
    fun getIsOnline(@Path("id") userId: String, @Query("t") time: String): Call<ResponseBody>

    @POST("https://online.alles.cx/")
    fun online(): Call<Void>

    @HEAD("https://avatar.alles.cx/u/{username}")
    fun getImageHeaders(@Path("username") username: String): Call<Void>

    @GET("post/{slug}?children?=")
    fun getThread(@Path("slug") slug: String): Call<AllesPost>

    @GET("post/{slug}")
    fun getPost(@Path("slug") slug: String): Call<AllesPost>
}