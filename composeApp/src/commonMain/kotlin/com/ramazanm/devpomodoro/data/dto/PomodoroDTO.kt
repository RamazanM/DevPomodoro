package com.ramazanm.devpomodoro.data.dto

import com.ramazanm.devpomodoro.data.db.entity.PomodoroEntity

data class PomodoroDTO(
    val id: Int? = null,
    val type: PomodoroType = PomodoroType.WORK,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val status: PomodoroStatus = PomodoroStatus.NOT_STARTED,
    val remainingSeconds: Long? = null,
    val taskId: Long? = -1
) {
    fun toEntity(): PomodoroEntity = PomodoroEntity(
        id = id,
        type = type,
        startTime = startTime,
        endTime = endTime,
        status = status,
        taskId = taskId
    )
}

enum class PomodoroType {
    //Dev Note: I removed Short&Long break logic from here because it shouldn't be tied to a pomodoro.
    //It should be tied to the session. So decide it when break starts.
    //If last 3 completed Breaks were Short, next one should be long break.
    //And we should define SESSION, like if it should be on app startup or a timeout etc.
    WORK, BREAK
}

enum class PomodoroStatus {
    NOT_STARTED, STARTED, PAUSED, FINISHED, INTERRUPTED, NOT_NEEDED
}