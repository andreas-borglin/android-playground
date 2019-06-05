package com.jakoon.playground

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

// Override to not use PlaygroundApp as we want to control Koin lifecycle and modules from test
class PlaygroundTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, Application::class.java.name, context)
    }
}