package com.jakoon.playground.test.shared

import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.model.Comment
import com.jakoon.playground.model.Post
import com.jakoon.playground.model.User
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.repository.Repository
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


class RepositoryTestActions(val repository: Repository) {

    fun mockGetPostsSuccess(posts: List<Post>) = runBlockingTest {
        `when`(repository.getPosts()).thenReturn(DataResult.Success(posts))
    }

    fun mockGetPostsFailure() = runBlockingTest {
        `when`(repository.getPosts()).thenReturn(DataResult.Failure(Exception()))
    }

    fun mockGetPostDetailsSuccess(post: Post) = runBlockingTest {
        `when`(repository.getPostDetails(post)).thenReturn(DataResult.Success(post))
    }

    fun mockGetPostDetailsFailure(post: Post) = runBlockingTest {
        `when`(repository.getPostDetails(post)).thenReturn(DataResult.Failure(Exception()))
    }

    fun verifyGetPostDetailsCalled(withPost: Post, numTimes: Int) = runBlockingTest {
        verify(repository, times(numTimes)).getPostDetails(withPost)
    }

    fun verifyGetPostsCalled(numTimes: Int) = runBlockingTest {
        verify(repository, times(numTimes)).getPosts()
    }

    fun verifyClearCacheCalled() = runBlockingTest {
        verify(repository).clearCache()
    }

}

class CacheTestActions(val cache: Cache) {

    fun mockEmptyCache() {
        `when`(cache.getCachedPosts()).thenReturn(null)
    }

    fun mockCachedPosts(posts: List<Post>) {
        `when`(cache.getCachedPosts()).thenReturn(posts)
    }

    fun verifyGetCachedPostsCalled() {
        verify(cache).getCachedPosts()
    }

    fun verifySetCachedPostsCalled(posts: List<Post>) {
        verify(cache).setCachedPosts(posts)
    }

    fun verifyCacheCleared() {
        verify(cache).clear()
    }
}

class ServiceTestActions(val typicodeService: TypicodeJsonService) {

    fun mockSuccessfulGetPosts(posts: List<Post>) = runBlockingTest {
        `when`(typicodeService.getPosts()).thenReturn(posts)
    }

    fun mockFailedGetPosts() = runBlockingTest {
        `when`(typicodeService.getPosts()).thenThrow(IllegalStateException::class.java)
    }

    fun mockSuccessfulGetPostDetails(user: User, comments: List<Comment>) = runBlockingTest {
        `when`(typicodeService.getUser(ArgumentMatchers.anyInt())).thenReturn(arrayListOf(user))
        `when`(typicodeService.getComments(ArgumentMatchers.anyInt())).thenReturn(comments)
    }

    fun mockFailedGetPostDetails() = runBlockingTest {
        `when`(typicodeService.getUser(ArgumentMatchers.anyInt())).thenThrow(IllegalStateException::class.java)
    }

    fun mockGetUser(userId: Int, user: User) = runBlockingTest {
        `when`(typicodeService.getUser(userId)).thenReturn(arrayListOf(user))
    }

    fun mockGetComments(id: Int, comments: List<Comment>) = runBlockingTest {
        `when`(typicodeService.getComments(id)).thenReturn(comments)
    }

    fun verifyGetPostsCalled(numTimes: Int = 1) = runBlockingTest {
        verify(typicodeService, times(numTimes)).getPosts()
    }

    fun verifyGetUser(userId: Int, numTimes: Int = 1) = runBlockingTest {
        verify(typicodeService, times(numTimes)).getUser(userId)
    }

    fun verifyGetComments(id: Int, numTimes: Int = 1) = runBlockingTest {
        verify(typicodeService, times(numTimes)).getComments(id)
    }
}