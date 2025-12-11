package com.ramazanm.devpomodoro.data.repository

import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO

abstract class TaskRepository() {
    abstract suspend fun getTask(id: Int): TaskDTO?
    abstract suspend fun addTask(taskDTO: TaskDTO)
    abstract suspend fun updateTask(taskDTO: TaskDTO)
    abstract suspend fun deleteTask(id: Int)
    abstract suspend fun getTasksWithPomodoros(): List<TaskWithPomodorosDTO>
    abstract suspend fun addPomodoro(pomodoroDTO: PomodoroDTO)
    abstract suspend fun updatePomodoro(pomodoroDTO: PomodoroDTO)

}