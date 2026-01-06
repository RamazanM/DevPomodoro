package com.ramazanm.devpomodoro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramazanm.devpomodoro.Routes
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.delete_task_success
import devpomodoro.composeapp.generated.resources.load_tasks_error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class TaskListState(
    val tasks: List<TaskWithPomodorosDTO> = listOf(), val isLoading: Boolean = true
)

open class TaskListEvent() {
    data class NavigateTo(val destination: Routes) : TaskListEvent()
    data class ShowToast(val message: StringResource) : TaskListEvent()
    data class ShowSnackBar(val message: StringResource) : TaskListEvent()
}

abstract class TaskListViewModel() : ViewModel() {
    abstract val taskListState: StateFlow<TaskListState>
    abstract val eventFlow: SharedFlow<TaskListEvent>

    abstract fun loadTasks()
}

class TaskListViewModelImpl(val repository: TaskRepository) : TaskListViewModel() {
    private val _taskListState = MutableStateFlow<TaskListState>(TaskListState(isLoading = true))
    override val taskListState: StateFlow<TaskListState> = _taskListState

    private val _eventFlow = MutableSharedFlow<TaskListEvent>()
    override val eventFlow: SharedFlow<TaskListEvent> = _eventFlow

    init {
        loadTasks()
    }

    override fun loadTasks() {
        viewModelScope.launch {
            _taskListState.update { it.copy(isLoading = true) }
            try {
                val tasks = repository.getTasksWithPomodoros()
                if (tasks.isEmpty()) {
                    _taskListState.update { TaskListState(isLoading = false) }
                } else {
                    _taskListState.update {
                        TaskListState(tasks, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _eventFlow.emit(TaskListEvent.ShowSnackBar(Res.string.load_tasks_error))
                _taskListState.update {
                    it.copy(isLoading = false)
                }
            }

        }

    }


}