package com.ramazanm.devpomodoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ramazanm.devpomodoro.data.db.entity.PomodoroEntity

@Dao
interface PomodoroDAO {
    @Insert
    suspend fun insert(item: PomodoroEntity)

    @Query("SELECT * FROM PomodoroEntity")
    suspend fun getAll(): List<PomodoroEntity>

    @Query("SELECT * FROM PomodoroEntity WHERE id = :id")
    suspend fun getById(id: Int): PomodoroEntity?

    @Query("SELECT * FROM PomodoroEntity WHERE taskId = :taskId")
    suspend fun getByTaskId(taskId: Int): List<PomodoroEntity>

    @Query("DELETE FROM PomodoroEntity WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun update(item: PomodoroEntity)
}

