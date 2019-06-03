package com.jakoon.playground.json

import com.jakoon.playground.api.Post
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

val moshi = Moshi.Builder().build()
val postAdapter: JsonAdapter<Post> = moshi.adapter(Post::class.java)

fun Post.toJson() = postAdapter.toJson(this)

fun postFromJson(json: String) = postAdapter.fromJson(json)