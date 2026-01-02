package com.ramazanm.devpomodoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import com.ramazanm.devpomodoro.data.db.entity.TaskWithPomodoros

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(item: TaskEntity): Long

    @Transaction
    @Query("SELECT * FROM taskEntity")
    suspend fun getAll(): List<TaskEntity>

    @Transaction
    @Query("SELECT * FROM taskEntity")
    suspend fun getAllTasksWithPomodoros(): List<TaskWithPomodoros>

    @Transaction
    @Query("SELECT * FROM taskEntity WHERE id=:id")
    suspend fun getTaskWithPomodoros(id: Long): TaskWithPomodoros

    @Transaction
    @Query("SELECT * FROM taskEntity WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Query("DELETE FROM taskEntity WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(item: TaskEntity)

    @Query("SELECT * FROM taskEntity WHERE status= 'STARTED'")
    suspend fun getActiveTask(): TaskWithPomodoros?
}

