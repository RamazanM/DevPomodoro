package com.ramazanm.devpomodoro.presentation

import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroType
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.load_tasks_error
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class TaskListViewModelTest {
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = TaskListViewModelImpl(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verify_loadTask_updates_taskListState_with_tasks() = runTest {
        coEvery { repository.getTasksWithPomodoros() } returns expectedTasksWithPomodoros
        val taskListStateList = mutableListOf<TaskListState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.taskListState.toList(taskListStateList)
        }
        viewModel.loadTasks()
        assertEquals(expectedTasksWithPomodoros, taskListStateList.last().tasks)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verify_loadTask_emits_error_snackbar_event_on_error() = runTest {
        coEvery { repository.getTasksWithPomodoros() } throws Exception()
        val eventList = mutableListOf<TaskListEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.toList(eventList)
        }
        viewModel.loadTasks()
        assertEquals(TaskListEvent.ShowSnackBar(Res.string.load_tasks_error), eventList.last())
    }
}

val expectedTasksWithPomodoros = listOf(
    TaskWithPomodorosDTO(
        TaskDTO(
            id = 1,
            title = "Test Task",
            description = "Test Description",
        ), listOf(
            PomodoroDTO(
                id = 1, type = PomodoroType.WORK, taskId = 1
            ), PomodoroDTO(
                id = 2, type = PomodoroType.BREAK, taskId = 1
            )
        )
    ), TaskWithPomodorosDTO(
        TaskDTO(
            id = 2,
            title = "Test Task",
            description = "Test Description",
        ), listOf(
            PomodoroDTO(
                id = 3, type = PomodoroType.WORK, taskId = 2
            ), PomodoroDTO(
                id = 4, type = PomodoroType.BREAK, taskId = 2
            )
        )
    )
)

