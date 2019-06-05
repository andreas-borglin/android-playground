package com.jakoon.playground.presentation.details

import com.jakoon.playground.presentation.ViewModelTestBase
import com.jakoon.playground.presentation.detail.PostDetailsViewModel
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.test.shared.post1
import com.jraska.livedata.test
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.koin.test.inject


class PostDetailsViewModelTest : ViewModelTestBase() {

    private val viewModel by inject<PostDetailsViewModel>()

    @Test
    fun `getPostsDetails() should fetch posts from repository only when livedata is empty`() = runBlockingTest {
        repositoryTestActions.mockGetPostDetailsSuccess(post1)

        viewModel.getPostDetails(post1).test().awaitValue()
        viewModel.getPostDetails(post1).test().awaitValue()

        repositoryTestActions.verifyGetPostDetailsCalled(withPost = post1, numTimes = 1)
    }

    @Test
    fun `getPostDetails() should return data as provided by repository`() = runBlockingTest {
        repositoryTestActions.mockGetPostDetailsSuccess(post1)

        val value = viewModel.getPostDetails(post1).value!!

        assertThat(value).isInstanceOf(DataResult.Success::class.java)
        assertThat((value as DataResult.Success).data).isEqualTo(post1)
    }

    @Test
    fun `getPosts() should re-fetch data if previous call returned failure`() = runBlockingTest {
        repositoryTestActions.mockGetPostDetailsFailure(post1)

        val value = viewModel.getPostDetails(post1).value!!
        assertThat(value).isInstanceOf(DataResult.Failure::class.java)

        repositoryTestActions.mockGetPostDetailsSuccess(post1)
        viewModel.getPostDetails(post1).test().awaitValue()

        repositoryTestActions.verifyGetPostDetailsCalled(withPost = post1, numTimes = 2)
    }
}