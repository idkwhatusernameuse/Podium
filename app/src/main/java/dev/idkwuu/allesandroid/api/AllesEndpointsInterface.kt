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
    fun vote(@Header("authorization") token: String, @Path("post") post: String, @Body vote: AllesVote): Call<AllesVote>

    @GET("mentions")
    fun getMentions(@Header("authorization") token: String): Call<AllesMentions>

    @GET("accounts")
    fun getAccounts(@Header("authorization") token: String): Call<AllesAccounts>

    @GET("post")
    fun post(@Header("authorization") token: String, @Body content: AllesInteractionPost): Call<AllesInteractionPost>

    @GET("post/{post}/remove")
    fun remove(@Header("authorization") token: String, @Path("post") post: String): Call<AllesInteractionRemove>
}