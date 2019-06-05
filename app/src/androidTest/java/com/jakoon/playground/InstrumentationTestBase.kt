package com.jakoon.playground

import androidx.test.platform.app.InstrumentationRegistry
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.di.appModule
import com.jakoon.playground.repository.Repository
import com.jakoon.playground.test.shared.ServiceTestActions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito.mock


abstract class InstrumentationTestBase : KoinTest {

    val service = mock(TypicodeJsonService::class.java)
    val serviceTestActions = ServiceTestActions(service)

    @Before
    fun setUp() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(appModule, module {
                single(override = true) { service }
                single(override = true) { Repository(service, get(), Dispatchers.Unconfined) }
            })
        }
        Dispatchers.setMain(Dispatchers.Unconfined) // Set main dispatcher to execute on *this* thread
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}