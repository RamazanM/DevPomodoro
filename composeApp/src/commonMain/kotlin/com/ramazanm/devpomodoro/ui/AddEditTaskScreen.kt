package com.ramazanm.devpomodoro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import com.ramazanm.devpomodoro.presentation.AddEditTaskState
import com.ramazanm.devpomodoro.presentation.AddEditTaskViewModel
import com.ramazanm.devpomodoro.presentation.TestAddEditTaskViewModelImpl
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun AddEditTaskScreen(viewModel: AddEditTaskViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isEditing by derivedStateOf { state.isEditMode }
    val datePickerState = rememberDatePickerState()

    if (!state.isLoading) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            Text(
                if (isEditing) "Edit Task" else "Add Task",
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.task.title,
                onValueChange = { viewModel.updateTaskState(state.task.copy(title = it)) },
                label = { Text("Title") })
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.task.description,
                onValueChange = { viewModel.updateTaskState(state.task.copy(description = it)) },
                label = { Text("Description") })
            DatePicker(datePickerState)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    val viewModel = TestAddEditTaskViewModelImpl()
    AddEditTaskScreen(viewModel)
}

@Preview(showBackground = true)
@Composable
fun EditTaskScreenPreview() {
    val viewModel = TestAddEditTaskViewModelImpl()
    LaunchedEffect(Unit) {
        viewModel._state.value = AddEditTaskState(
            TaskDTO(
                1, "Task 1", "Description 1", TaskStatus.STARTED, TaskSourceType.LOCAL,
                Clock.System.now().epochSeconds,
                Clock.System.now().epochSeconds, 1
            )
        )
    }
    AddEditTaskScreen(viewModel)
}