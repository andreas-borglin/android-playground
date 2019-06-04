package com.jakoon.playground.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakoon.playground.*
import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.di.appModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
class RepositoryTest : KoinTest {

    val typicodeService by inject<TypicodeJsonService>()
    val cache by inject<Cache>()
    lateinit var repository: Repository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        startKoin { modules(appModule, testModule) }
        repository = Repository(typicodeService, cache, Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    fun mockNoCache() {
        Mockito.`when`(cache.getCachedPosts()).thenReturn(null)
    }

    fun mockCache() {
        Mockito.`when`(cache.getCachedPosts()).thenReturn(testPosts)
    }

    fun mockSuccessfulGetPosts() = runBlockingTest {
        Mockito.`when`(typicodeService.getPosts()).thenReturn(testPosts)
    }

    fun mockFailedGetPosts() = runBlockingTest {
        Mockito.`when`(typicodeService.getPosts()).thenThrow(IllegalStateException::class.java)
    }

    fun mockSuccessfulGetPostDetails() = runBlockingTest {
        Mockito.`when`(typicodeService.getUser(anyInt())).thenReturn(testUsers)
        Mockito.`when`(typicodeService.getComments(anyInt())).thenReturn(testComments)
    }

    fun mockFailedGetPostDetails() = runBlockingTest {
        Mockito.`when`(typicodeService.getUser(anyInt())).thenThrow(IllegalStateException::class.java)
    }

    @Test
    fun `should clear cache`() {
        repository.clearCache()

        verify(cache).clear()
    }

    @Test
    fun `getPosts() should fetch from service if not cached`() = runBlockingTest {
        mockNoCache()
        mockSuccessfulGetPosts()

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        verify(typicodeService).getPosts()
    }

    @Test
    fun `getPosts() should use cached posts if available`() = runBlockingTest {
        mockCache()

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        verify(cache).getCachedPosts()
        verify(typicodeService, never()).getPosts()
    }

    @Test
    fun `getPosts() should cache fetched posts`() = runBlockingTest {
        mockNoCache()
        mockSuccessfulGetPosts()

        repository.getPosts()

        verify(cache).setCachedPosts(testPosts)
    }

    @Test
    fun `getPosts() should return failure if API call throws exception`() = runBlockingTest {
        mockNoCache()
        mockFailedGetPosts()

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Failure::class.java)
    }

    @Test
    fun `getPostDetails() should return input parameter if details are cached`() = runBlockingTest {

        val result = repository.getPostDetails(testPostWithDetails)

        assertThat(result).isEqualTo(DataResult.Success(testPostWithDetails))
        verify(typicodeService, never()).getUser(anyInt())
    }

    @Test
    fun `getPostDetails() should fetch user and comments from API service`() = runBlockingTest {
        mockSuccessfulGetPostDetails()

        val result = repository.getPostDetails(testPost)

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        val post = (result as DataResult.Success).data
        assertThat(post.user).isEqualTo(fakeUser)
        assertThat(post.comments).isEqualTo(testComments)

        verify(typicodeService).getUser(testPost.userId)
        verify(typicodeService).getComments(testPost.id)
    }

    @Test
    fun `getPostDetails() should return failure if API call throws exception`() = runBlockingTest {
        mockFailedGetPostDetails()

        val result = repository.getPostDetails(testPost)

        assertThat(result).isInstanceOf(DataResult.Failure::class.java)
    }
}