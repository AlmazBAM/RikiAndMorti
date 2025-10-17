package com.bagmanovam.rikiandmorti

import android.app.Application
import com.bagmanovam.rikiandmorti.di.appModule
import com.bagmanovam.rikiandmorti.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                appModule, dataModule
            )
        }
    }
}