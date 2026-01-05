package com.ramazanm.devpomodoro.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ramazanm.devpomodoro.Routes
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.taskDTONavTypeMapper
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.delete_task_success
import devpomodoro.composeapp.generated.resources.save_task_success_add
import devpomodoro.composeapp.generated.resources.save_task_success_update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.Instant

data class ValidationState(
    val title: String? = null,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val isFormValid: Boolean = true
)

data class AddEditTaskState(
    val task: TaskDTO = TaskDTO(),
    val validationState: ValidationState? = ValidationState(),
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false
)

open class AddEditTaskEvent() {
    object NavigateBack : AddEditTaskEvent()
    data class ShowToast(val message: StringResource) : AddEditTaskEvent()
    data class ShowSnackBar(val message: StringResource) : AddEditTaskEvent()
}

abstract class AddEditTaskViewModel() : ViewModel() {
    abstract val state: StateFlow<AddEditTaskState>
    abstract fun updateTaskState(task: TaskDTO)
    abstract suspend fun saveTask()
    abstract suspend fun deleteTask()
}

class AddEditTaskViewModelImpl(
    savedStateHandle: SavedStateHandle, val repository: TaskRepository
) : AddEditTaskViewModel() {
    val taskFromRoute: TaskDTO? =
        savedStateHandle.toRoute<Routes.AddEditTaskScreen>(typeMap = taskDTONavTypeMapper).taskDTO
    private val _state = MutableStateFlow<AddEditTaskState>(AddEditTaskState(isLoading = true))
    override val state: StateFlow<AddEditTaskState> = _state
    private val _eventFlow = MutableSharedFlow<AddEditTaskEvent>()
    val eventFlow: SharedFlow<AddEditTaskEvent> = _eventFlow

    init {
        _state.value =
            AddEditTaskState(
                task = taskFromRoute?: TaskDTO(), isEditMode = taskFromRoute != null,
                validationState = ValidationState(),
                isLoading = false
            )

    }

    override fun updateTaskState(task: TaskDTO) {
        _state.update { it.copy(task = task) }
    }

    private fun validateTask(): Boolean {
        var isFormValid = true
        state.value.task.let { taskDTO ->
            val validationState = ValidationState(
                title = when {
                    taskDTO.title.isBlank() -> {
                        isFormValid = false
                        "Title cannot be empty"
                    }

                    taskDTO.title.length > 120 -> {
                        isFormValid = false
                        "Title cannot be longer than 120 characters"
                    }

                    else -> null
                }, description = when {
                    taskDTO.description.isBlank() -> {
                        isFormValid = false
                        "Description cannot be empty"
                    }

                    else -> null
                }, startDate = when {
                    taskDTO.startDate.asInstant()
                        .isBefore(Clock.System.now()) -> "Start date cannot be before today."

                    else -> null
                }, endDate = when {
                    taskDTO.endDate.asInstant()
                        .isBefore(Clock.System.now()) -> "End date cannot be before today."

                    else -> null
                }, isFormValid = isFormValid
            )
            (_state).update {
                it.copy(validationState = validationState)
            }
        }
        return isFormValid
    }

    override suspend fun saveTask() {
        if (!validateTask()) return
        state.value.task.let {
            if (state.value.isEditMode) {
                repository.updateTask(it)
                _eventFlow.emit(AddEditTaskEvent.ShowToast(Res.string.save_task_success_update))
                _eventFlow.emit(AddEditTaskEvent.NavigateBack)
            } else {
                repository.addTask(it)
                _eventFlow.emit(AddEditTaskEvent.ShowToast(Res.string.save_task_success_add))
                _eventFlow.emit(AddEditTaskEvent.NavigateBack)
            }
        }
    }

    override suspend fun deleteTask() {
        if (state.value.isEditMode) {
            state.value.task.id?.let { repository.deleteTask(it) }
            _eventFlow.emit(AddEditTaskEvent.ShowSnackBar(Res.string.delete_task_success))
            _eventFlow.emit(AddEditTaskEvent.NavigateBack)
        } else {
            _eventFlow.emit(AddEditTaskEvent.NavigateBack)
        }
    }
}

private fun Instant.isBefore(other: Instant) = other.minus(this).inWholeMilliseconds > 0

private fun Long.asInstant() = Instant.fromEpochMilliseconds(this)


class TestAddEditTaskViewModelImpl() : AddEditTaskViewModel() {
    val _state = MutableStateFlow(AddEditTaskState(isLoading = true))
    override val state: StateFlow<AddEditTaskState> = _state
    override fun updateTaskState(task: TaskDTO) {
    }

    override suspend fun saveTask() {
    }

    override suspend fun deleteTask() {
    }

}