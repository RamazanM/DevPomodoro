package com.ramazanm.devpomodoro.data.repository

import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO

abstract class TaskRepository() {
    abstract suspend fun getTask(id: Long): TaskDTO?
    abstract suspend fun addTask(taskDTO: TaskDTO): Long
    abstract suspend fun updateTask(taskDTO: TaskDTO)
    abstract suspend fun deleteTask(id: Long)
    abstract suspend fun getTasksWithPomodoros(): List<TaskWithPomodorosDTO>
    abstract suspend fun addPomodoro(pomodoroDTO: PomodoroDTO)
    abstract suspend fun updatePomodoro(pomodoroDTO: PomodoroDTO)

    abstract suspend fun getTaskWithPomodoros(taskId: Long): TaskWithPomodorosDTO?
    abstract suspend fun increaseEstimation(taskDTO: TaskDTO)
    abstract suspend fun decreaseEstimation(taskDTO: TaskDTO)
    abstract suspend fun getActiveTaskWithPomodoros(): TaskWithPomodorosDTO?
    abstract suspend fun addTaskWithEstimation(taskDTO: TaskDTO, estimation: Int)
    abstract suspend fun completeTask(taskDTO: TaskDTO)
    abstract suspend fun completePomodoro(pomodoroDTO: PomodoroDTO)
    abstract suspend fun cancelPomodoro(pomodoroDTO: PomodoroDTO)
    abstract suspend fun setActiveTask(taskDTO: TaskDTO)
}