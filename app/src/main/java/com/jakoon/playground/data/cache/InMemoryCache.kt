package com.jakoon.playground.data.cache

import com.jakoon.playground.model.Post

class InMemoryCache : Cache {

    private var postsCache: List<Post>? = null

    override fun getCachedPosts(): List<Post>? {
        return postsCache
    }

    override fun setCachedPosts(posts: List<Post>) {
        postsCache = posts
    }

    override fun clear() {
        postsCache = null
    }

}