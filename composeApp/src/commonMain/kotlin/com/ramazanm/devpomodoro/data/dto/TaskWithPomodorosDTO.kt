package com.ramazanm.devpomodoro.data.dto

data class TaskWithPomodorosDTO(
    val task: TaskDTO,
    val pomodoros: List<PomodoroDTO>
)
