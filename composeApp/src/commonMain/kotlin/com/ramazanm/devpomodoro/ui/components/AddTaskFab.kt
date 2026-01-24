package com.ramazanm.devpomodoro.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.ramazanm.devpomodoro.util.TestTags.ADD_TASK_FAB

@Composable
fun AddTaskFab(selectedItem: Routes, onClick: () -> Unit) {
    AnimatedVisibility(
        selectedItem == Routes.TaskListScreen,
        enter = slideInHorizontally { it * 2 },
        exit = slideOutHorizontally { it * 2 })
    {
        FloatingActionButton(
            modifier = Modifier.testTag(ADD_TASK_FAB),
            shape = CircleShape,
            onClick = onClick
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}