package com.ramazanm.devpomodoro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.taskDTONavTypeMapper
import com.ramazanm.devpomodoro.ui.AddEditTaskScreen
import com.ramazanm.devpomodoro.ui.TaskListScreen
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.title_add_task
import devpomodoro.composeapp.generated.resources.title_edit_task
import devpomodoro.composeapp.generated.resources.title_my_tasks
import devpomodoro.composeapp.generated.resources.title_pomodoro
import devpomodoro.composeapp.generated.resources.title_settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                    Routes.AddEditTaskScreen(null)
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

val RouteSaver = Saver<Routes, String>(
    save = { it.serialize() },
    restore = {
        Routes.deserialize(it)
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    var selectedItem: Routes = rememberSaveable(saver = RouteSaver) { Routes.TaskListScreen }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text(selectedItem.title()) }) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedItem == Routes.PomodoroScreen,
                        onClick = {
                            selectedItem = Routes.PomodoroScreen
                            navController.navigate(Routes.PomodoroScreen)
                        },
                        icon = { Icon(Icons.Default.Timer, contentDescription = "Add") },
                        label = { Text("Pomodoro") }
                    )
                    NavigationBarItem(
                        selected = selectedItem == Routes.TaskListScreen,
                        onClick = {
                            selectedItem = Routes.TaskListScreen
                            navController.navigate(Routes.TaskListScreen)
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Add") },
                        label = { Text("Tasks") }
                    )

                    NavigationBarItem(
                        selected = selectedItem == Routes.SettingsScreen,
                        onClick = {
                            selectedItem = Routes.SettingsScreen
                            navController.navigate(Routes.SettingsScreen)
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Add") },
                        label = { Text("Settings") }
                    )
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    selectedItem == Routes.TaskListScreen,
                    enter = slideInHorizontally { it * 2 },
                    exit = slideOutHorizontally { it * 2 })
                {
                    FloatingActionButton(shape = CircleShape, onClick = {
                        navController.navigate(
                            Routes.AddEditTaskScreen(
                                null
                            )
                        )
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,

            ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                navController = navController,
                startDestination = Routes.TaskListScreen
            ) {
                composable<Routes.TaskListScreen> {
                    TaskListScreen()
                }
                composable<Routes.PomodoroScreen> {
                    Text("Pomodoro")
                }
                composable<Routes.SettingsScreen> {
                    Text("Settings")
                }
                composable<Routes.AddEditTaskScreen>(
                    typeMap = taskDTONavTypeMapper
                ) {
                    AddEditTaskScreen()
                }
            }
        }
    }
}
