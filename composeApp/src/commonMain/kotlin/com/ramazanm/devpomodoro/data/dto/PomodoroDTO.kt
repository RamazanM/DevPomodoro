package com.ramazanm.devpomodoro.data.dto

import com.ramazanm.devpomodoro.data.db.entity.PomodoroEntity

data class PomodoroDTO(
    val id: Int,
    val type: PomodoroType,
    val startTime: Long,
    val endTime: Long,
    val status: PomodoroStatus,
    val duration: Long,
    val taskId: Int
){
    fun toEntity(): PomodoroEntity = PomodoroEntity(
        id = id,
        type = type,
        startTime = startTime,
        endTime = endTime,
        status = status,
        duration = duration,
        taskId = taskId
    )
}

enum class PomodoroType {
    WORK, SHORT_BREAK, LONG_BREAK
}

enum class PomodoroStatus {
    STARTED, PAUSED, FINISHED, INTERRUPTED
}