package com.ramazanm.devpomodoro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramazanm.devpomodoro.data.dto.taskDTONavTypeMapper
import com.ramazanm.devpomodoro.ui.AddEditTaskScreen
import com.ramazanm.devpomodoro.ui.TaskListScreen
import com.ramazanm.devpomodoro.ui.components.AddTaskFab
import com.ramazanm.devpomodoro.ui.components.AppNavigationBar
import com.ramazanm.devpomodoro.ui.components.RouteSaver
import com.ramazanm.devpomodoro.ui.components.Routes
import com.ramazanm.devpomodoro.ui.components.title
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    var selectedItem: Routes by rememberSaveable(saver = RouteSaver) { mutableStateOf(Routes.TaskListScreen) }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text(selectedItem.title()) }) },
            bottomBar = {
                AppNavigationBar(selectedItem) {
                    selectedItem = it
                    navController.navigate(selectedItem)
                }
            },
            floatingActionButton = {
                AddTaskFab(selectedItem) {
                    navController.navigate(
                        Routes.AddEditTaskScreen(
                            null
                        )
                    )
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
