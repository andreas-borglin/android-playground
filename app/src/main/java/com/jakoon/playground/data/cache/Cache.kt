package com.jakoon.playground.data.cache

import com.jakoon.playground.model.Post

interface Cache {

    fun getCachedPosts(): List<Post>?

    fun setCachedPosts(posts: List<Post>)

    fun clear()
}