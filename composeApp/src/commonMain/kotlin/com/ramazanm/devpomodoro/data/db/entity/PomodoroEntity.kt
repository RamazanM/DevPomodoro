package com.ramazanm.devpomodoro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.PomodoroType

@Entity
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val type: PomodoroType,
    val startTime: Long?,
    val endTime: Long?,
    val status: PomodoroStatus,
    val taskId: Long?
){
    fun toDTO(): PomodoroDTO = PomodoroDTO(
        id = id,
        type = type,
        startTime = startTime,
        endTime = endTime,
        status = status,
        taskId = taskId
    )
}
