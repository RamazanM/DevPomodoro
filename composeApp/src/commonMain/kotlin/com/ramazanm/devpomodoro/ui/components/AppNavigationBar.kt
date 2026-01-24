package com.ramazanm.devpomodoro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.ramazanm.devpomodoro.util.TestTags

@Composable
fun AppNavigationBar(selectedItem: Routes, onClick: (Routes) -> Unit) = NavigationBar {
    NavigationBarItem(
        modifier = Modifier.testTag(TestTags.POMODORO_NAVBAR_ITEM),
        selected = selectedItem == Routes.PomodoroScreen,
        onClick = { onClick(Routes.PomodoroScreen) },
        icon = { Icon(Icons.Default.Timer, contentDescription = "Add") },
        label = { Text("Pomodoro") })
    NavigationBarItem(
        modifier = Modifier.testTag(TestTags.TASK_LIST_NAVBAR_ITEM),
        selected = selectedItem == Routes.TaskListScreen,
        onClick = {
            onClick(Routes.TaskListScreen)
        },
        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Add") },
        label = { Text("Tasks") })

    NavigationBarItem(
        modifier = Modifier.testTag(TestTags.SETTINGS_NAVBAR_ITEM),
        selected = selectedItem == Routes.SettingsScreen,
        onClick = {
            onClick(Routes.SettingsScreen)
        },
        icon = { Icon(Icons.Default.Settings, contentDescription = "Add") },
        label = { Text("Settings") })
}