package com.jakoon.playground.repository

import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(val apiService: TypicodeJsonService, val cache: Cache) {

    suspend fun getPosts(): DataRetrievalOutcome<Post> = withContext(Dispatchers.IO) {
        getData(cache.posts) { apiService.getPosts() }
    }

    suspend fun getUsers(): DataRetrievalOutcome<User> = withContext(Dispatchers.IO) {
        getData(cache.users) { apiService.getUsers() }
    }

    suspend fun getComments(): DataRetrievalOutcome<Comment> = withContext(Dispatchers.IO) {
        getData(cache.comments) { apiService.getComments() }
    }

    private inline fun <reified T> getData(
        cachedData: MutableList<T>,
        apiCall: () -> List<T>
    ): DataRetrievalOutcome<T> {
        return try {
            if (cachedData.isNotEmpty()) {
                DataRetrievalOutcome.Success(cachedData)
            } else {
                val list = apiCall()
                cachedData.addAll(list)
                DataRetrievalOutcome.Success(list)
            }
        } catch (t: Throwable) {
            DataRetrievalOutcome.Failure(t)
        }
    }

}