package com.jakoon.playground.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakoon.playground.di.appModule
import com.jakoon.playground.model.Post
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.repository.Repository
import com.jakoon.playground.testModule
import com.jraska.livedata.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
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
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.*

@ExperimentalCoroutinesApi
class ListPostsViewModelTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val repository by inject<Repository>()
    private val viewModel by inject<ListPostsViewModel>()
    private val testPost = Post(1, 2, "title", "body")

    @Before
    fun setUp() {
        startKoin { modules(appModule, testModule) }
        Dispatchers.setMain(Unconfined) // Set main dispatcher to execute on *this* thread
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    fun mockRepositorySuccess() = runBlockingTest {
        Mockito.`when`(repository.getPosts()).thenReturn(DataResult.Success(Arrays.asList(testPost)))
    }

    fun mockRepositoryFailure() = runBlockingTest {
        Mockito.`when`(repository.getPosts()).thenReturn(DataResult.Failure(Exception()))
    }

    @Test
    fun `getPosts() should fetch posts from API service only when no data is cached`() = runBlockingTest {
        mockRepositorySuccess()

        viewModel.getPosts().test().awaitValue()
        viewModel.getPosts().test().awaitValue()

        verify(repository, times(1)).getPosts()
    }

    @Test
    fun `getPosts() should return data as provided by API service`() = runBlockingTest {
        mockRepositorySuccess()

        val value = viewModel.getPosts().value!!

        assertThat(value).isInstanceOf(DataResult.Success::class.java)
        assertThat((value as DataResult.Success).list[0]).isEqualTo(testPost)
    }

    @Test
    fun `refreshPosts() should call API service with refresh flag set`() = runBlockingTest {
        mockRepositorySuccess()

        viewModel.refreshPosts()

        verify(repository, times(1)).getPosts(refresh = true)
    }

    @Test
    fun `getPosts() should re-fetch data if previous call returned failure`() = runBlockingTest {
        mockRepositoryFailure()

        val value = viewModel.getPosts().value!!
        assertThat(value).isInstanceOf(DataResult.Failure::class.java)

        mockRepositorySuccess()
        viewModel.getPosts().test().awaitValue()

        verify(repository, times(2)).getPosts()
    }
}