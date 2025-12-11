package com.ramazanm.devpomodoro.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.ramazanm.devpomodoro.data.db.AppDatabase
import java.io.File

const val DATABASE_NAME = "DevPomodoro.db"


fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.dir"), DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}