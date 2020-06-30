package com.example.sukodu.app

import android.app.Application
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(Modules.modules)
        }
    }
}