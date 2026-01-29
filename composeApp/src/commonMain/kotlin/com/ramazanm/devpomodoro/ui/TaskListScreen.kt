package com.ramazanm.devpomodoro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.presentation.TaskListEvent
import com.ramazanm.devpomodoro.presentation.TaskListState
import com.ramazanm.devpomodoro.presentation.TaskListViewModel
import com.ramazanm.devpomodoro.util.ContentDescriptions
import com.ramazanm.devpomodoro.util.TaskListScreenPreviewTestTasks
import com.ramazanm.devpomodoro.util.TestTags
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.desc_no_task
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun TaskListScreen(viewModel: TaskListViewModel = koinViewModel()) {
    val taskListState by viewModel.taskListState.collectAsStateWithLifecycle()
    val taskList = taskListState.tasks

    Column {
        if (taskList.isEmpty()) {
            Text(stringResource(Res.string.desc_no_task))
        }
        LazyColumn {
            items(taskList) {
                TasklistItem(Modifier.testTag(TestTags.TASK_LIST_ITEM), it)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun TaskListScreenPreview() {
    val taskList = TaskListScreenPreviewTestTasks
    TaskListScreen(object : TaskListViewModel() {
        override val taskListState: StateFlow<TaskListState> =
            MutableStateFlow(TaskListState(taskList))
        override val eventFlow: SharedFlow<TaskListEvent> = MutableSharedFlow()

        override fun loadTasks() {
        }

    })
}


@Composable
fun TasklistItem(modifier: Modifier = Modifier, taskWithPomodorosDTO: TaskWithPomodorosDTO) {
    Row(modifier = modifier) {
        Column {
            Text(
                modifier = Modifier.testTag(TestTags.TASK_TITLE),
                text = taskWithPomodorosDTO.task.title
            )
            Text(
                modifier = Modifier.testTag(TestTags.TASK_DESCRIPTION),
                text = taskWithPomodorosDTO.task.description
            )
        }
        Row(Modifier.testTag(TestTags.POMODORO_DETAILS)) {
            Icon(Icons.Filled.Timer, ContentDescriptions.POMODORO_COUNT)
            if(taskWithPomodorosDTO.task.status== TaskStatus.STARTED){
                Row {
                    for (pomodoro in taskWithPomodorosDTO.pomodoros) {
                        Icon(Icons.Filled.Timer, contentDescription = when (pomodoro.status) {
                            PomodoroStatus.STARTED -> ContentDescriptions.POMODORO_STARTED
                            PomodoroStatus.NOT_STARTED -> ContentDescriptions.POMODORO_NOT_STARTED
                            PomodoroStatus.FINISHED -> ContentDescriptions.POMODORO_FINISHED
                            PomodoroStatus.PAUSED -> TODO()
                            PomodoroStatus.INTERRUPTED -> TODO()
                            PomodoroStatus.NOT_NEEDED -> TODO()
                        })
                    }

                }
            }
            else {
                Text("x${taskWithPomodorosDTO.pomodoros.size}")
            }
        }
    }
}