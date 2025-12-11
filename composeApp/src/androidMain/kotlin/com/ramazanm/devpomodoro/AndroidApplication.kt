package com.ramazanm.devpomodoro

import android.app.Application
import com.ramazanm.devpomodoro.di.commonModule
import com.ramazanm.devpomodoro.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidApplication)
            modules(commonModule,platformModule())
        }
    }
}