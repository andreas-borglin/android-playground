package com.jakoon.playground.api

import retrofit2.http.GET

interface TypicodeJsonService {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("comments")
    suspend fun getComments(): List<Comment>

}