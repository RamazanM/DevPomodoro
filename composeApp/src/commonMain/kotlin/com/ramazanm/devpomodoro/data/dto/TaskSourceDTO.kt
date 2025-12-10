package com.ramazanm.devpomodoro.data.dto

data class TaskSourceDTO(
    val id: Int,
    val name: String,
    val description: String,
    val sourceType: TaskSourceType,
    val connectionUrl: String?,
    val key: String?,
)
