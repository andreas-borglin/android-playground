package com.jakoon.playground.presentation.details

import com.jakoon.playground.presentation.ViewModelTestBase
import com.jakoon.playground.presentation.detail.PostDetailsViewModel
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.testPost
import com.jraska.livedata.test
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Test
import org.koin.test.inject
import org.mockito.Mockito


class PostDetailsViewModelTest : ViewModelTestBase() {

    private val viewModel by inject<PostDetailsViewModel>()

    fun mockRepositorySuccess() = runBlockingTest {
        Mockito.`when`(repository.getPostDetails(testPost)).thenReturn(DataResult.Success(testPost))
    }

    fun mockRepositoryFailure() = runBlockingTest {
        Mockito.`when`(repository.getPostDetails(testPost)).thenReturn(DataResult.Failure(Exception()))
    }

    @Test
    fun `getPostsDetails() should fetch posts from repository only when livedata is empty`() = runBlockingTest {
        mockRepositorySuccess()

        viewModel.getPostDetails(testPost).test().awaitValue()
        viewModel.getPostDetails(testPost).test().awaitValue()

        Mockito.verify(repository, Mockito.times(1)).getPostDetails(testPost)
    }

    @Test
    fun `getPostDetails() should return data as provided by repository`() = runBlockingTest {
        mockRepositorySuccess()

        val value = viewModel.getPostDetails(testPost).value!!

        Assertions.assertThat(value).isInstanceOf(DataResult.Success::class.java)
        Assertions.assertThat((value as DataResult.Success).data).isEqualTo(testPost)
    }

    @Test
    fun `getPosts() should re-fetch data if previous call returned failure`() = runBlockingTest {
        mockRepositoryFailure()

        val value = viewModel.getPostDetails(testPost).value!!
        Assertions.assertThat(value).isInstanceOf(DataResult.Failure::class.java)

        mockRepositorySuccess()
        viewModel.getPostDetails(testPost).test().awaitValue()

        Mockito.verify(repository, Mockito.times(2)).getPostDetails(testPost)
    }
}