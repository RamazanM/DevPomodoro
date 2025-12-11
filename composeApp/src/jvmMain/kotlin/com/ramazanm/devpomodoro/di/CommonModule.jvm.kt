package com.ramazanm.devpomodoro.di

import androidx.room.RoomDatabase
import com.ramazanm.devpomodoro.data.db.AppDatabase
import com.ramazanm.devpomodoro.data.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule():Module{
    return module {
        single<RoomDatabase.Builder<AppDatabase>> {
            getDatabaseBuilder()
        }
    }
}

