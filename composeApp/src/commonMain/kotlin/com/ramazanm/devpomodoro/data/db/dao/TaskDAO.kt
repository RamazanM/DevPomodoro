package com.ramazanm.devpomodoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import com.ramazanm.devpomodoro.data.db.entity.TaskWithPomodoros

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(item: TaskEntity)
    @Query("SELECT * FROM taskEntity")
    suspend fun getAll(): List<TaskEntity>
    @Query("SELECT * FROM taskEntity")
    suspend fun getAllTasksWithPomodoros(): List<TaskWithPomodoros>
    @Query("SELECT * FROM taskEntity WHERE id = :id")
    suspend fun getById(id: Int): TaskEntity?
    @Query("DELETE FROM taskEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
    @Update
    suspend fun update(item: TaskEntity)
}

