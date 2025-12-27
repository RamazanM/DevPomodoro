package com.ramazanm.devpomodoro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import kotlin.time.Clock

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val title: String="",
    val description: String="",
    val status: String="",
    val source: TaskSourceType= TaskSourceType.LOCAL,
    val startDate: Long= Clock.System.now().epochSeconds,
    val endDate: Long=Clock.System.now().epochSeconds,
    val priority: Int=1,
){
    fun toDTO(): TaskDTO = TaskDTO(
        id = id,
        title = title,
        description = description,
        status = status,
        source = source,
        startDate = startDate,
        endDate = endDate,
        priority = priority
    )
}
