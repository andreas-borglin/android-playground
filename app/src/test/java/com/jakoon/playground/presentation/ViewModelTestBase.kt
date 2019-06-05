package com.jakoon.playground.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakoon.playground.di.appModule
import com.jakoon.playground.repository.Repository
import com.jakoon.playground.test.shared.RepositoryTestActions
import com.jakoon.playground.testModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject


abstract class ViewModelTestBase : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val repository by inject<Repository>()
    lateinit var repositoryTestActions: RepositoryTestActions

    @Before
    fun setUp() {
        startKoin { modules(appModule, testModule) }
        repositoryTestActions = RepositoryTestActions(repository)
        Dispatchers.setMain(Dispatchers.Unconfined) // Set main dispatcher to execute on *this* thread
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}