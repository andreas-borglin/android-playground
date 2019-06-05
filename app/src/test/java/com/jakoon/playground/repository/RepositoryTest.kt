package com.jakoon.playground.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.di.appModule
import com.jakoon.playground.test.shared.*
import com.jakoon.playground.testModule
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
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
class RepositoryTest : KoinTest {

    val typicodeService by inject<TypicodeJsonService>()
    val cache by inject<Cache>()
    lateinit var repository: Repository
    lateinit var serviceTestActions: ServiceTestActions
    lateinit var cacheTestActions: CacheTestActions

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        startKoin { modules(appModule, testModule) }
        repository = Repository(typicodeService, cache, Dispatchers.Unconfined)
        serviceTestActions = ServiceTestActions(typicodeService)
        cacheTestActions = CacheTestActions(cache)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should clear cache`() {
        repository.clearCache()

        verify(cache).clear()
    }

    @Test
    fun `getPosts() should fetch from service if not cached`() = runBlockingTest {
        cacheTestActions.mockEmptyCache()
        serviceTestActions.mockSuccessfulGetPosts(postsList)

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        serviceTestActions.verifyGetPostsCalled()
    }

    @Test
    fun `getPosts() should use cached posts if available`() = runBlockingTest {
        cacheTestActions.mockCachedPosts(postsList)

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        cacheTestActions.verifyGetCachedPostsCalled()
        serviceTestActions.verifyGetPostsCalled(0)
    }

    @Test
    fun `getPosts() should cache fetched posts`() = runBlockingTest {
        cacheTestActions.mockEmptyCache()
        serviceTestActions.mockSuccessfulGetPosts(postsList)

        repository.getPosts()

        cacheTestActions.verifySetCachedPostsCalled(postsList)
    }

    @Test
    fun `getPosts() should return failure if API call throws exception`() = runBlockingTest {
        cacheTestActions.mockEmptyCache()
        serviceTestActions.mockFailedGetPosts()

        val result = repository.getPosts()

        assertThat(result).isInstanceOf(DataResult.Failure::class.java)
    }

    @Test
    fun `getPostDetails() should return input Post object if details are already set`() = runBlockingTest {

        val result = repository.getPostDetails(postWithDetails)

        assertThat(result).isEqualTo(DataResult.Success(postWithDetails))
        serviceTestActions.verifyGetUser(postWithDetails.userId, 0)
    }

    @Test
    fun `getPostDetails() should fetch user and comments from API service`() = runBlockingTest {
        serviceTestActions.mockSuccessfulGetPostDetails(user, comments)

        val result = repository.getPostDetails(post1)

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        val post = (result as DataResult.Success).data
        assertThat(post.user).isEqualTo(user)
        assertThat(post.comments).isEqualTo(comments)

        serviceTestActions.verifyGetUser(post1.userId)
        serviceTestActions.verifyGetComments(post1.id)
    }

    @Test
    fun `getPostDetails() should return failure if API call throws exception`() = runBlockingTest {
        serviceTestActions.mockFailedGetPostDetails()

        val result = repository.getPostDetails(post1)

        assertThat(result).isInstanceOf(DataResult.Failure::class.java)
    }
}