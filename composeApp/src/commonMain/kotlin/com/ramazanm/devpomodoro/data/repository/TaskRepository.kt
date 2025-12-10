package com.ramazanm.devpomodoro.data.repository

import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceDTO

abstract class TaskRepository(taskSourceDTO: TaskSourceDTO){
    abstract suspend fun getTasks(): List<TaskDTO>
    abstract suspend fun getTask(id: Int): TaskDTO
    abstract suspend fun addTask(taskDTO: TaskDTO)
    abstract suspend fun updateTask(taskDTO: TaskDTO)
    abstract suspend fun deleteTask(id: Int)
}