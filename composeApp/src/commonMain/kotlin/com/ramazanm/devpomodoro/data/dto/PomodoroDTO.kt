package com.ramazanm.devpomodoro.data.dto

data class PomodoroDTO(
    val id: Int,
    val type: PomodoroType,
    val startTime: Long,
    val endTime: Long,
    val status: PomodoroStatus,
    val duration: Long,
)

enum class PomodoroType {
    WORK, SHORT_BREAK, LONG_BREAK
}

enum class PomodoroStatus {
    STARTED, PAUSED, FINISHED, INTERRUPTED
}