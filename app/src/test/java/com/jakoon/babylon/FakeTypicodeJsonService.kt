package com.jakoon.babylon

import com.jakoon.babylon.api.Comment
import com.jakoon.babylon.api.Post
import com.jakoon.babylon.api.TypicodeJsonService
import com.jakoon.babylon.api.User

class FakeTypicodeJsonService(
    var posts: List<Post> = ArrayList(),
    var users: List<User> = ArrayList(),
    var comments: List<Comment> = ArrayList(),
    var throwException: Boolean = false
) : TypicodeJsonService {

    override suspend fun getPosts(): List<Post> {
        if (throwException) throw RuntimeException("API exception")
        return posts
    }

    override suspend fun getUsers(): List<User> {
        if (throwException) throw RuntimeException("API exception")
        return users
    }

    override suspend fun getComments(): List<Comment> {
        if (throwException) throw RuntimeException("API exception")
        return comments
    }
}