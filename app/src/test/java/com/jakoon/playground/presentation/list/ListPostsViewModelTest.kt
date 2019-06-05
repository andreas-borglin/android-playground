package com.jakoon.playground.presentation.list

import com.jakoon.playground.presentation.ViewModelTestBase
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.test.shared.post1
import com.jraska.livedata.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ListPostsViewModelTest : ViewModelTestBase() {

    private val viewModel by inject<ListPostsViewModel>()

    @Test
    fun `getPosts() should fetch posts from repository only when livedata is empty`() = runBlockingTest {
        repositoryTestActions.mockGetPostsSuccess(arrayListOf(post1))

        viewModel.getPosts().test().awaitValue()
        viewModel.getPosts().test().awaitValue()

        repositoryTestActions.verifyGetPostsCalled(numTimes = 1)
    }

    @Test
    fun `getPosts() should return data as provided by repository`() = runBlockingTest {
        repositoryTestActions.mockGetPostsSuccess(arrayListOf(post1))

        val value = viewModel.getPosts().value!!

        assertThat(value).isInstanceOf(DataResult.Success::class.java)
        assertThat((value as DataResult.Success).data[0]).isEqualTo(post1)
    }

    @Test
    fun `refreshPosts() should clear cache and fetch posts`() = runBlockingTest {
        repositoryTestActions.mockGetPostsSuccess(arrayListOf(post1))

        viewModel.refreshPosts()

        repositoryTestActions.verifyClearCacheCalled()
        repositoryTestActions.verifyGetPostsCalled(numTimes = 1)
    }

    @Test
    fun `getPosts() should re-fetch data if previous call returned failure`() = runBlockingTest {
        repositoryTestActions.mockGetPostsFailure()

        val value = viewModel.getPosts().value!!
        assertThat(value).isInstanceOf(DataResult.Failure::class.java)

        repositoryTestActions.mockGetPostsSuccess(arrayListOf(post1))
        viewModel.getPosts().test().awaitValue()

        repositoryTestActions.verifyGetPostsCalled(numTimes = 2)
    }
}