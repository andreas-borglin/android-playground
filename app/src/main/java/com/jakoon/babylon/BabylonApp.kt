package com.jakoon.babylon

import android.app.Application
import com.jakoon.babylon.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class BabylonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BabylonApp)
            modules(appModule)
        }
    }
}