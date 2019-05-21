package com.jakoon.babylon

import com.jakoon.babylon.api.TypicodeJsonService
import org.koin.dsl.module
import org.mockito.Mockito.mock

val testModule = module {
    single(override = true) { mock(TypicodeJsonService::class.java) as TypicodeJsonService }
}