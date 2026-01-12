package com.ramazanm.devpomodoro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.load_pomodoro_error
import devpomodoro.composeapp.generated.resources.load_tasks_error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration

data class PomodoroState(
    val selectedTask: TaskWithPomodorosDTO? = null,
    var activePomodoro: PomodoroDTO? = null,
    val isLoading: Boolean = true
)

open class PomodoroEvent {
    object NextTask : PomodoroEvent()
    data class ShowToast(val toastMessageRes: StringResource) : PomodoroEvent()
}

abstract class PomodoroViewModel : ViewModel() {
    abstract val pomodoroState: StateFlow<PomodoroState>
    abstract val eventFlow: SharedFlow<PomodoroEvent>
    abstract fun fetchCurrentPomodoro()
    abstract fun startPomodoro()
    abstract fun pausePomodoro(remainingTime: Duration)
    abstract fun skipPomodoro()
}

class PomodoroViewModelImpl(val repository: TaskRepository) : PomodoroViewModel() {
    private val _pomodoroState = MutableStateFlow(PomodoroState(isLoading = true))
    override val pomodoroState: StateFlow<PomodoroState> = _pomodoroState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<PomodoroEvent>()
    override val eventFlow: SharedFlow<PomodoroEvent> = _eventFlow.asSharedFlow()

    override fun fetchCurrentPomodoro() {
        viewModelScope.launch {
            val activeTask = repository.getActiveTaskWithPomodoros()
            if (activeTask == null) {
                _eventFlow.emit(PomodoroEvent.ShowToast(Res.string.load_tasks_error))
            } else {
                _pomodoroState.update {
                    it.copy(
                        selectedTask = activeTask,
                        activePomodoro = activeTask.pomodoros.find { pomodoro ->
                            pomodoro.status == PomodoroStatus.STARTED
                                    || pomodoro.status == PomodoroStatus.PAUSED
                                    || pomodoro.status == PomodoroStatus.INTERRUPTED
                                    || pomodoro.status == PomodoroStatus.NOT_STARTED
                        },
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun startPomodoro() {
        viewModelScope.launch {
            val activePomodoro = pomodoroState.value.activePomodoro

            if (activePomodoro == null) {
                _eventFlow.emit(PomodoroEvent.ShowToast(Res.string.load_pomodoro_error))
            } else {
                repository.updatePomodoro(
                    activePomodoro.copy(
                        status = PomodoroStatus.STARTED
                    )
                )
            }
            fetchCurrentPomodoro()
        }
    }

    override fun pausePomodoro(remainingTime: Duration) {
        viewModelScope.launch {
            val activePomodoro = pomodoroState.value.activePomodoro

            if (activePomodoro == null) {
                _eventFlow.emit(PomodoroEvent.ShowToast(Res.string.load_pomodoro_error))
            } else {
                repository.updatePomodoro(
                    activePomodoro.copy(
                        remainingSeconds = remainingTime.inWholeSeconds,
                        status = PomodoroStatus.PAUSED
                    )
                )
                fetchCurrentPomodoro()
            }
        }
    }

    override fun skipPomodoro() {
        viewModelScope.launch {
            val activePomodoro = pomodoroState.value.activePomodoro

            if (activePomodoro == null) {
                _eventFlow.emit(PomodoroEvent.ShowToast(Res.string.load_pomodoro_error))
            } else {
                repository.updatePomodoro(
                    activePomodoro.copy(
                        status = PomodoroStatus.NOT_NEEDED
                    )
                )
                if (activePomodoro == pomodoroState.value.selectedTask?.pomodoros?.last()) {
                    _eventFlow.emit(PomodoroEvent.NextTask)
                }
                fetchCurrentPomodoro()
            }
        }
    }
}