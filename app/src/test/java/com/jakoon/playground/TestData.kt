package com.jakoon.playground

import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User
import org.mockito.Mockito.mock
import java.util.*

val testPost = Post(1, 2, "title", "body")
val testPosts = Arrays.asList(testPost)

val fakeUser = mock(User::class.java)
val testUsers = Arrays.asList(fakeUser)
val fakeComment = mock(Comment::class.java)
val testComments = Arrays.asList(fakeComment)
val testPostWithDetails = testPost.copy(user = fakeUser, comments = testComments)