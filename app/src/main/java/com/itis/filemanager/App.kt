package com.itis.filemanager

import android.app.Application
import com.itis.filemanager.di.appModule
import com.itis.filemanager.di.featureModule
import com.itis.filemanager.di.filesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                featureModule,
                filesModule
            )
        }
    }
}
