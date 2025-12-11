package com.ramazanm.devpomodoro.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO

data class TaskWithPomodoros(
    @Embedded
    val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val pomodoros: List<PomodoroEntity>
){
    fun toDTO(): TaskWithPomodorosDTO = TaskWithPomodorosDTO(
        task = task.toDTO(),
        pomodoros = pomodoros.map { it.toDTO() }
    )

}
