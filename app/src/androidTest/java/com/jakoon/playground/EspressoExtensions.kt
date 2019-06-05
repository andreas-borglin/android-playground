package com.jakoon.playground

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*


fun assertViewsHaveText(vararg viewTextArgs: Pair<Int, String?>) {
    viewTextArgs.forEach {
        onView(withId(it.first)).check(matches(withText(it.second)))
    }
}

fun assertViewsHaveTextRes(vararg viewTextArgs: Pair<Int, Int>) {
    viewTextArgs.forEach {
        onView(withId(it.first)).check(matches(withText(it.second)))
    }
}

fun assertTextIsDisplayed(vararg texts: String) {
    texts.forEach { onView(withText(it)).check(matches(isDisplayed())) }
}

fun assertTextIsDisplayed(textRes: Int) {
    onView(withText(textRes)).check(matches(isDisplayed()))
}

fun assertTextIsNotDisplayed(vararg texts: String) {
    texts.forEach { onView(withText(it)).check(doesNotExist()) }
}

fun swipeDownView(view: Int) {
    onView(withId(view)).perform(swipeDown())
}

fun clickOn(viewText: String) {
    onView(withText(viewText)).perform(click())
}