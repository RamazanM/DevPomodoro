package com.ramazanm.devpomodoro.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}

fun getTaskSourceDAO(database: AppDatabase): TaskSourceDAO {
    return database.taskSourceDao()
}
fun getTaskDAO(database: AppDatabase): TaskDAO {
    return database.taskDao()
}
fun getPomodoroDAO(database: AppDatabase): PomodoroDAO {
    return database.pomodoroDao()
}