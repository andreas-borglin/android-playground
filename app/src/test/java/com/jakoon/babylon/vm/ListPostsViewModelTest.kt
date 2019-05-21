package com.jakoon.babylon.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakoon.babylon.api.Post
import com.jakoon.babylon.api.TypicodeJsonService
import com.jakoon.babylon.di.appModule
import com.jakoon.babylon.testModule
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

    val apiService by inject<TypicodeJsonService>()
    val viewModel by inject<ListPostsViewModel>()
    val testPost = Post(1, 2, "title", "body")

    @Before
    fun setUp() {
        startKoin { modules(appModule, testModule) }
        Dispatchers.setMain(Unconfined) // Set main dispatcher to execute on *this* thread
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    fun mockServicePosts() = runBlockingTest {
        Mockito.`when`(apiService.getPosts()).thenReturn(Arrays.asList(testPost))
    }

    @Test
    fun `getPosts() should fetch posts from API service only when no data is cached`() = runBlockingTest {
        mockServicePosts()

        viewModel.getPosts().test().awaitValue()
        viewModel.getPosts().test().awaitValue()

        verify(apiService, times(1)).getPosts()
    }

    @Test
    fun `getPosts() should return data as provided by API service`() = runBlockingTest {
        mockServicePosts()

        val value = viewModel.getPosts().value

        assertThat(value).isNotNull()
        assertThat(value!![0]).isEqualTo(testPost)
    }

    @Test
    fun `refreshPosts() should call API service`() = runBlockingTest {
        mockServicePosts()

        viewModel.refreshPosts()

        verify(apiService, times(1)).getPosts()
    }
}