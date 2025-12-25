package com.ramazanm.devpomodoro.data.dto

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class TaskDTO(
    val id: Int?=null,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val source: TaskSourceType = TaskSourceType.LOCAL,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val priority: Int = 0,
) {
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

val taskDTONavTypeMapper=mapOf(typeOf<TaskDTO?>() to object : NavType<TaskDTO?>(true) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: TaskDTO?
    ) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): TaskDTO? {
        return Json.decodeFromString<TaskDTO?>(bundle.read { getString(key) })
    }

    override fun parseValue(value: String): TaskDTO? {
        return if (value == "null") null else Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: TaskDTO?): String {
        return value?.let { Json.encodeToString(it) } ?: "null"
    }
})