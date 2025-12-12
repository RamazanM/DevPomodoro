package com.ramazanm.devpomodoro.data.dto

import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val source: TaskSourceType,
    val startDate: Long,
    val endDate: Long,
    val priority: Int,
){
    fun toEntity(): TaskEntity = TaskEntity(
        id = id,
        title = title,
        description = description,
        status = status,
        source = source,
        startDate = startDate,
        endDate = endDate,
        priority = priority,
    )
}
