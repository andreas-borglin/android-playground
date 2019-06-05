package com.jakoon.playground.test.shared

import com.jakoon.playground.model.Address
import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User

val post1 = Post(1, 2, "post 1", "post 1 body")
val post2 = Post(2, 3, "post 2", "post 2 body")
val postsList = arrayListOf(post1, post2)
val user = User(1, "User", "user", "user@email.com", Address("st", "1", "L", "1"), "123", ".com")
val comment = Comment(1, 2, "User", "user@email.com", "This is a comment")
val comments = arrayListOf(comment)
val postWithDetails = post1.copy(user = user, comments = arrayListOf(comment))