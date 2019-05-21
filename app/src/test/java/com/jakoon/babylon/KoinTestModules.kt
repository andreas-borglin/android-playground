package com.jakoon.babylon

import com.jakoon.babylon.api.TypicodeJsonService
import org.koin.dsl.module


val testModule = module {
    single { FakeTypicodeJsonService() as TypicodeJsonService }
}