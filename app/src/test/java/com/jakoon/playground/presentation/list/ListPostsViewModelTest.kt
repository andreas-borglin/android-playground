package com.jakoon.playground.presentation.list

import com.jakoon.playground.presentation.ViewModelTestBase
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.testPost
import com.jraska.livedata.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.koin.test.inject
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.*

@ExperimentalCoroutinesApi
class ListPostsViewModelTest : ViewModelTestBase() {

    private val viewModel by inject<ListPostsViewModel>()

    fun mockRepositorySuccess() = runBlockingTest {
        Mockito.`when`(repository.getPosts()).thenReturn(DataResult.Success(Arrays.asList(testPost)))
    }

    fun mockRepositoryFailure() = runBlockingTest {
        Mockito.`when`(repository.getPosts()).thenReturn(DataResult.Failure(Exception()))
    }

    @Test
    fun `getPosts() should fetch posts from repository only when livedata is empty`() = runBlockingTest {
        mockRepositorySuccess()

        viewModel.getPosts().test().awaitValue()
        viewModel.getPosts().test().awaitValue()

        verify(repository, times(1)).getPosts()
    }

    @Test
    fun `getPosts() should return data as provided by repository`() = runBlockingTest {
        mockRepositorySuccess()

        val value = viewModel.getPosts().value!!

        assertThat(value).isInstanceOf(DataResult.Success::class.java)
        assertThat((value as DataResult.Success).data[0]).isEqualTo(testPost)
    }

    @Test
    fun `refreshPosts() should clear cache and fetch posts`() = runBlockingTest {
        mockRepositorySuccess()

        viewModel.refreshPosts()

        verify(repository, times(1)).clearCache()
        verify(repository, times(1)).getPosts()
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