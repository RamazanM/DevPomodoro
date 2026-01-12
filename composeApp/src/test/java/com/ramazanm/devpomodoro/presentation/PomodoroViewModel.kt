package com.ramazanm.devpomodoro.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration

data class PomodoroState(
    val selectedTask: TaskDTO,
    var activePomodoro: PomodoroDTO
)

open class PomodoroEvent {
    object NextTask : PomodoroEvent()
    class ShowToast(@StringRes toastMessageRes: StringResource) : PomodoroEvent()
}

abstract class PomodoroViewModel : ViewModel() {
    abstract val pomodoroState: StateFlow<PomodoroState>
    abstract val eventFlow: SharedFlow<PomodoroEvent>
    abstract fun fetchCurrentPomodoro()
    abstract fun startPomodoro()
    abstract fun pausePomodoro(remainingTime: Duration)
    abstract fun skipPomodoro()
    abstract fun getNextPomodoro()
}