package com.jakoon.playground.presentation.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import com.jakoon.playground.InstrumentationTestBase
import com.jakoon.playground.R
import com.jakoon.playground.assertViewsHaveText
import com.jakoon.playground.assertViewsHaveTextRes
import com.jakoon.playground.json.toJson
import com.jakoon.playground.model.Post
import com.jakoon.playground.test.shared.comments
import com.jakoon.playground.test.shared.post1
import com.jakoon.playground.test.shared.postWithDetails
import com.jakoon.playground.test.shared.user
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Integration test driven from PostDetailsFragment with a mocked API service at the end to simulate and verify desired behaviour
 */
class PostDetailsFragmentTest : InstrumentationTestBase() {

    private fun startFragment(post: Post) {
        val fragmentArgs = Bundle().apply {
            putString("post", post.toJson())
        }
        launchFragmentInContainer<PostDetailsFragment>(fragmentArgs)
    }

    @Test
    fun shouldShowPostWithPredefinedDetails() = runBlockingTest {
        startFragment(postWithDetails)

        assertViewsHaveText(
            R.id.title to postWithDetails.title,
            R.id.author to postWithDetails.user?.name,
            R.id.body to postWithDetails.body,
            R.id.comments to postWithDetails.comments?.size.toString()
        )

        serviceTestActions.verifyGetUser(postWithDetails.userId, 0)
        serviceTestActions.verifyGetComments(postWithDetails.id, 0)
    }

    @Test
    fun shouldShowPostWithDetailsFromService() = runBlockingTest {
        serviceTestActions.mockGetUser(post1.userId, user)
        serviceTestActions.mockGetComments(post1.id, comments)

        startFragment(post1)

        assertViewsHaveText(
            R.id.title to post1.title,
            R.id.author to user.name,
            R.id.body to post1.body,
            R.id.comments to comments.size.toString()
        )

        serviceTestActions.verifyGetUser(post1.userId, 1)
        serviceTestActions.verifyGetComments(post1.id, 1)
    }

    @Test
    fun shouldDisplayErrorMessageOnApiServiceException() = runBlockingTest {
        serviceTestActions.mockFailedGetPostDetails()

        startFragment(post1)

        assertViewsHaveTextRes(R.id.title to R.string.post_details_failed)
    }
}