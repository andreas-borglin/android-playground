package com.jakoon.playground.data.cache

import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User

class InMemoryCache : Cache {

    override var posts = mutableListOf<Post>()
    override var users = mutableListOf<User>()
    override var comments = mutableListOf<Comment>()

}