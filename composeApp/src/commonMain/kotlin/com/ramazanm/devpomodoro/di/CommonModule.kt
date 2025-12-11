package com.ramazanm.devpomodoro.di

import com.ramazanm.devpomodoro.data.db.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module
val commonModule = module {
    single { getRoomDatabase(get()) }
}