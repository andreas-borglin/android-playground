package com.jakoon.playground.data.cache

import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User

// Very naive implementation of a cache - just for showcasing the use of repository
interface Cache {

    val posts: MutableList<Post>
    val users: MutableList<User>
    val comments: MutableList<Comment>

}