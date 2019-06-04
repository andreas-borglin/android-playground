package com.jakoon.playground

import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.repository.Repository
import org.koin.dsl.module
import org.mockito.Mockito.mock

val testModule = module {
    single(override = true) { mock(TypicodeJsonService::class.java) as TypicodeJsonService }
    single(override = true) { mock(Repository::class.java) }
}