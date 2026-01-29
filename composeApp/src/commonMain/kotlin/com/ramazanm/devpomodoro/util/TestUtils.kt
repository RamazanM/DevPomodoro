package com.ramazanm.devpomodoro.util

import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.PomodoroType
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

object TestTags {
    const val TASK_LIST_ITEM = "TaskListItem"
    const val TASK_TITLE = "TaskTitle"
    const val TASK_DESCRIPTION = "TaskDescription"
    const val POMODORO_DETAILS = "PomodoroDetails"
    const val ADD_TASK_FAB = "AddTaskFab"
    const val POMODORO_NAVBAR_ITEM = "PomodoroNavBarItem"
    const val TASK_LIST_NAVBAR_ITEM = "TaskListNavBarItem"
    const val SETTINGS_NAVBAR_ITEM = "SettingsNavBarItem"
    const val POMODORO_PROGRESS = "PomodoroProgress"

}

object ContentDescriptions {
    const val POMODORO_COUNT = "Pomodoro Count"
    const val POMODORO_STARTED = "Pomodoro Started"
    const val POMODORO_NOT_STARTED = "Pomodoro Not Started"
    const val POMODORO_FINISHED = "Pomodoro Finished"
}

val TaskListScreenPreviewTestTasks = listOf(
    TaskWithPomodorosDTO(
        TaskDTO(
            1, "Task 1", "Description 1", TaskStatus.STARTED, TaskSourceType.LOCAL,
            Clock.System.now().epochSeconds,
            Clock.System.now().plus(3.days).epochSeconds, 1
        ), listOf(
            PomodoroDTO(
                1,
                PomodoroType.WORK,
                Clock.System.now().epochSeconds,
                Clock.System.now().plus(3.days).epochSeconds,
                PomodoroStatus.STARTED,
                1
            )
        )
    ),
    TaskWithPomodorosDTO(
        TaskDTO(
            2, "Task 2", "Description 2", TaskStatus.STARTED, TaskSourceType.LOCAL,
            Clock.System.now().epochSeconds,
            Clock.System.now().plus(3.days).epochSeconds, 1
        ), listOf(
            PomodoroDTO(
                2,
                PomodoroType.WORK,
                Clock.System.now().epochSeconds,
                Clock.System.now().plus(3.days).epochSeconds,
                PomodoroStatus.STARTED,
                1
            )
        )
    )
)