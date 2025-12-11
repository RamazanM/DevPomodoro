package com.ramazanm.devpomodoro.di

import com.ramazanm.devpomodoro.data.db.getPomodoroDAO
import com.ramazanm.devpomodoro.data.db.getRoomDatabase
import com.ramazanm.devpomodoro.data.db.getTaskDAO
import com.ramazanm.devpomodoro.data.repository.RoomTaskRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module
val commonModule = module {
    single { getRoomDatabase(get()) }
    single { getTaskDAO(get()) }
    single { getPomodoroDAO(get()) }

    single { RoomTaskRepository(get(), get()) }
}