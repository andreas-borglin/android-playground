package com.jakoon.playground.data.network

import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface TypicodeJsonService {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUser(@Query("id") id: Int): List<User>

    @GET("comments")
    suspend fun getComments(@Query("postId") id: Int): List<Comment>

}