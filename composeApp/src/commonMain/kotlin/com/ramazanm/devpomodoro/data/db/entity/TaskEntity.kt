package com.ramazanm.devpomodoro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val title: String,
    val description: String,
    val status: String,
    val source: TaskSourceType,
    val startDate: Long,
    val endDate: Long,
    val priority: Int,
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
