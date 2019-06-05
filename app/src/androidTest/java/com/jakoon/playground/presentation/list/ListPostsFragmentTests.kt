package com.jakoon.playground.presentation.list

import androidx.fragment.app.testing.launchFragmentInContainer
import com.jakoon.playground.*
import com.jakoon.playground.test.shared.post1
import com.jakoon.playground.test.shared.post2
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Integration test driven from ListPostsFragment with a mocked API service at the end to simulate and verify desired behaviour
 */
class ListPostsFragmentTests : InstrumentationTestBase() {

    private fun startFragment() {
        launchFragmentInContainer<ListPostsFragment>()
    }

    @Test
    fun shouldShowErrorMessageIfApiServiceThrowsException() = runBlockingTest {
        serviceTestActions.mockFailedGetPosts()

        startFragment()

        assertViewsHaveTextRes(R.id.error_message to R.string.posts_failed)
    }

    @Test
    fun shouldShowListOfTitlesReturnedByApiService() = runBlockingTest {
        serviceTestActions.mockSuccessfulGetPosts(arrayListOf(post1, post2))

        startFragment()

        assertTextIsDisplayed(post1.title, post2.title)
    }

    @Test
    fun shouldShowNewPostsOnSwipeToRefresh() = runBlockingTest {
        // Setup initial post and validate it's shown
        serviceTestActions.mockSuccessfulGetPosts(arrayListOf(post1))

        startFragment()
        assertTextIsDisplayed(post1.title)

        // Set up second post
        serviceTestActions.mockSuccessfulGetPosts(arrayListOf(post2))

        // Now swipe to refresh
        swipeDownView(R.id.swipe_to_refresh)

        // Validate old post not visible, but new one is
        assertTextIsDisplayed(post2.title)
        assertTextIsNotDisplayed(post1.title)
    }
}
