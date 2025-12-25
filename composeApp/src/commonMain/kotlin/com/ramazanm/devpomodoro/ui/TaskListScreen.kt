package com.ramazanm.devpomodoro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.PomodoroType
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@Composable
fun TaskListScreen(taskList: List<TaskWithPomodorosDTO>) {
    Column {
        Text("Task list", style = MaterialTheme.typography.headlineLarge)
        LazyColumn {
            items(taskList) {
                TasklistItem(it,)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun TaskListScreenPreview() {
    val taskList = listOf(
        TaskWithPomodorosDTO(
            TaskDTO(
                1, "Task 1", "Description 1", "In progress", TaskSourceType.LOCAL,
                Clock.System.now().epochSeconds,
                Clock.System.now().plus(3.days).epochSeconds, 1
            ), listOf(
                PomodoroDTO(
                    1,
                    PomodoroType.WORK,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().plus(3.days).epochSeconds,
                    PomodoroStatus.STARTED,
                    3.hours.inWholeMilliseconds,
                    1
                )
            )
        ),
        TaskWithPomodorosDTO(
            TaskDTO(
                2, "Task 2", "Description 2", "In progress", TaskSourceType.LOCAL,
                Clock.System.now().epochSeconds,
                Clock.System.now().plus(3.days).epochSeconds, 1
            ), listOf(
                PomodoroDTO(
                    2,
                    PomodoroType.WORK,
                    Clock.System.now().epochSeconds,
                    Clock.System.now().plus(3.days).epochSeconds,
                    PomodoroStatus.STARTED,
                    3.hours.inWholeMilliseconds,
                    1
                )
            )
        )
    )
    TaskListScreen(taskList)
}

@Composable
fun TasklistItem(taskWithPomodorosDTO: TaskWithPomodorosDTO) {
    Row {
        Column {
            Text(taskWithPomodorosDTO.task.title)
            Text(taskWithPomodorosDTO.task.description)
        }
    }

}