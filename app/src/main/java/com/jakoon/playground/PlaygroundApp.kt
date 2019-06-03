package com.jakoon.playground

import android.app.Application
import com.jakoon.playground.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PlaygroundApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaygroundApp)
            modules(appModule)
        }
    }
}