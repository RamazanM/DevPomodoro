package com.ramazanm.devpomodoro.data.dto

data class TaskDTO(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val source: TaskSourceType,
    val startDate: Long,
    val endDate: Long,
    val priority: Int,
    val pomodoros: List<PomodoroDTO>
)
