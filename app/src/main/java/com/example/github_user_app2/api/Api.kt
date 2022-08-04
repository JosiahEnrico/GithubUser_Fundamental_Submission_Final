package com.example.github_user_app2.api

import com.example.github_user_app2.data.main.DetailResponse
import com.example.github_user_app2.data.main.User
import com.example.github_user_app2.data.main.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}