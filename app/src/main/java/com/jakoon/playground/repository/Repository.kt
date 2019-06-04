package com.jakoon.playground.repository

import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.model.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext


class Repository(val apiService: TypicodeJsonService, val cache: Cache, val dispatcher: CoroutineDispatcher) {

    fun clearCache() = cache.clear()

    suspend fun getPosts(): DataResult<List<Post>> = withContext(dispatcher) {
        cache.getCachedPosts()?.let { DataResult.Success(it) }
            ?: getData(cache = { cache.setCachedPosts(it) }, apiCall = { apiService.getPosts() })
    }

    suspend fun getPostDetails(post: Post): DataResult<Post> = withContext(dispatcher) {
        if (post.hasDetails()) {
            DataResult.Success(post)
        } else {
            // TODO this scope allows us to return failure on exception - but is it the right way?
            supervisorScope {
                try {
                    val userCall = async { apiService.getUser(post.userId) }
                    val commentsCall = async { apiService.getComments(post.id) }

                    post.user = userCall.await()[0]
                    post.comments = commentsCall.await()
                    DataResult.Success(post)
                } catch (t: Throwable) {
                    DataResult.Failure<Post>(t)
                }
            }
        }
    }

    // TODO unnecessary allocation of lamdba for cache when not used
    private inline fun <T> getData(cache: (T) -> Unit = {}, apiCall: () -> T): DataResult<T> {
        return try {
            val list = apiCall()
            cache(list)
            DataResult.Success(list)
        } catch (t: Throwable) {
            DataResult.Failure(t)
        }
    }

}