package com.ramazanm.devpomodoro.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.title_add_task
import devpomodoro.composeapp.generated.resources.title_edit_task
import devpomodoro.composeapp.generated.resources.title_my_tasks
import devpomodoro.composeapp.generated.resources.title_pomodoro
import devpomodoro.composeapp.generated.resources.title_settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource

@Serializable
sealed class Routes() {
    @Serializable
    data object TaskListScreen : Routes()

    @Serializable
    data object PomodoroScreen : Routes()

    @Serializable
    data object SettingsScreen : Routes()

    @Serializable
    data class AddEditTaskScreen(val taskDTO: TaskDTO?) : Routes()

    fun serialize(): String {
        println("SERIALIZER: " + this::class.simpleName)
        return this::class.simpleName + when (this) {
            is Routes.AddEditTaskScreen -> "|" + Json.encodeToString(this.taskDTO)
            else -> "|"
        }
    }

    companion object {
        fun <T : Routes> deserialize(serialized: String): T {
            val className = serialized.split("|")[0]
            val dataSerialized = serialized.split("|")[1]
            return when (className) {
                "AddEditTaskScreen" -> {
                    val data = Json.decodeFromString<TaskDTO?>(dataSerialized)
                    Routes.AddEditTaskScreen(data)
                }

                "TaskListScreen" -> Routes.TaskListScreen
                "PomodoroScreen" -> Routes.PomodoroScreen
                "SettingsScreen" -> Routes.SettingsScreen

                else -> Routes.TaskListScreen
            } as T
        }

    }
}

@Composable
fun Routes.title(): String {
    return stringResource(
        when (this) {
            is Routes.TaskListScreen -> Res.string.title_my_tasks
            is Routes.AddEditTaskScreen -> if (this.taskDTO == null) Res.string.title_add_task else Res.string.title_edit_task
            is Routes.PomodoroScreen -> Res.string.title_pomodoro
            is Routes.SettingsScreen -> Res.string.title_settings
        }
    )
}


val RouteSaver = Saver<MutableState<Routes>, String>(
    save = { it.value.serialize() },
    restore = {
        mutableStateOf(Routes.deserialize(it))
    }
)