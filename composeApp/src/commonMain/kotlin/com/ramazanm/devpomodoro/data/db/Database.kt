package com.ramazanm.devpomodoro.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ramazanm.devpomodoro.data.db.dao.PomodoroDAO
import com.ramazanm.devpomodoro.data.db.dao.TaskDAO
import kotlinx.coroutines.Dispatchers


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}


fun getTaskDAO(database: AppDatabase): TaskDAO {
    return database.taskDao()
}
fun getPomodoroDAO(database: AppDatabase): PomodoroDAO {
    return database.pomodoroDao()
}