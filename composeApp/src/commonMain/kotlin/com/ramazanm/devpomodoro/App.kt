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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.presentation.AppViewModel
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Serializable
open class Routes {
    @Serializable
    data object TaskListScreen : Routes()

    @Serializable
    data object PomodoroScreen : Routes()

    @Serializable
    data object SettingsScreen : Routes()

    @Serializable
    data class AddEditTaskScreen(val taskDTO: TaskDTO?) : Routes()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val viewModel: AppViewModel = koinViewModel()
    val items by viewModel.taskListState.collectAsState()
    val navController = rememberNavController()
    val selectedItem = rememberSaveable { mutableStateOf(Routes.TaskListScreen.toString()) }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Dev Pomodoro") }) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedItem.value == Routes.PomodoroScreen.toString(),
                        onClick = {
                            selectedItem.value = Routes.PomodoroScreen.toString()
                            navController.navigate(Routes.PomodoroScreen)
                        },
                        icon = { Icon(Icons.Default.Timer, contentDescription = "Add") },
                        label = { Text("Pomodoro") }
                    )
                    NavigationBarItem(
                        selected = selectedItem.value == Routes.TaskListScreen.toString(),
                        onClick = {
                            selectedItem.value = Routes.TaskListScreen.toString()
                            navController.navigate(Routes.TaskListScreen)
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Add") },
                        label = { Text("Tasks") }
                    )

                    NavigationBarItem(
                        selected = selectedItem.value == Routes.SettingsScreen.toString(),
                        onClick = {
                            selectedItem.value = Routes.SettingsScreen.toString()
                            navController.navigate(Routes.SettingsScreen)
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Add") },
                        label = { Text("Settings") }
                    )
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    selectedItem.value == Routes.TaskListScreen.toString(),
                    enter = slideInHorizontally { it*2 },
                    exit = slideOutHorizontally { it*2 })
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
                    Text("Task list")
                }
                composable<Routes.PomodoroScreen> {
                    Text("Pomodoro")
                }
                composable<Routes.SettingsScreen> {
                    Text("Settings")
                }
            }
        }
    }
}