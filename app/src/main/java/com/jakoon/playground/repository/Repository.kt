package com.jakoon.playground.repository

import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(val apiService: TypicodeJsonService, val cache: Cache) {

    suspend fun getPosts(refresh: Boolean = false): DataResult<Post> = withContext(Dispatchers.IO) {
        getData(refresh, cache.posts) { apiService.getPosts() }
    }

    suspend fun getUsers(refresh: Boolean = false): DataResult<User> = withContext(Dispatchers.IO) {
        getData(refresh, cache.users) { apiService.getUsers() }
    }

    suspend fun getComments(refresh: Boolean = false): DataResult<Comment> = withContext(Dispatchers.IO) {
        getData(refresh, cache.comments) { apiService.getComments() }
    }

    private inline fun <reified T> getData(
        forceRefresh: Boolean,
        cachedData: MutableList<T>,
        apiCall: () -> List<T>
    ): DataResult<T> {
        return try {
            if (!forceRefresh && cachedData.isNotEmpty()) {
                DataResult.Success(cachedData)
            } else {
                val list = apiCall()
                cachedData.addAll(list)
                DataResult.Success(list)
            }
        } catch (t: Throwable) {
            DataResult.Failure(t)
        }
    }

}