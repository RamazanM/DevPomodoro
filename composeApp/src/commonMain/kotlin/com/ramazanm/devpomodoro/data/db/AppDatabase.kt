package com.ramazanm.devpomodoro.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ramazanm.devpomodoro.data.db.dao.PomodoroDAO
import com.ramazanm.devpomodoro.data.db.dao.TaskDAO
import com.ramazanm.devpomodoro.data.db.entity.PomodoroEntity
import com.ramazanm.devpomodoro.data.db.entity.TaskEntity

@Database(entities = [TaskEntity::class, PomodoroEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDAO
    abstract fun pomodoroDao(): PomodoroDAO
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}