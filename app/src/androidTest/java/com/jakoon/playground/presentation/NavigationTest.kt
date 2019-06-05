package com.jakoon.playground.presentation

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.jakoon.playground.*
import com.jakoon.playground.test.shared.postWithDetails
import org.junit.Rule
import org.junit.Test


class NavigationTest : InstrumentationTestBase() {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, false)

    val launchIntent: Intent? = null

    private fun startActivity() = activityRule.launchActivity(launchIntent)

    @Test
    fun shouldNavigateToPostDetails() {
        serviceTestActions.mockSuccessfulGetPosts(arrayListOf(postWithDetails))
        startActivity()

        clickOn(postWithDetails.title)

        assertTextIsDisplayed(R.string.post_details_title)
        assertViewsHaveText(
            R.id.title to postWithDetails.title,
            R.id.author to postWithDetails.user?.name,
            R.id.body to postWithDetails.body,
            R.id.comments to postWithDetails.comments?.size.toString()
        )
    }

    @Test
    fun shouldNavigateBackToPostsList() {
        serviceTestActions.mockSuccessfulGetPosts(arrayListOf(postWithDetails))
        startActivity()
        clickOn(postWithDetails.title)

        assertTextIsDisplayed(R.string.post_details_title)
        onView(ViewMatchers.withContentDescription("Navigate up")).perform(click())

        assertTextIsDisplayed(R.string.posts_title)
    }
}