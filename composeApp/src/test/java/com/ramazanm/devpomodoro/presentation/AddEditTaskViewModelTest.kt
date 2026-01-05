package com.ramazanm.devpomodoro.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.testing.invoke
import com.ramazanm.devpomodoro.Routes
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.taskDTONavTypeMapper
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import io.mockk.coVerify
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
import kotlin.reflect.KType
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddEditTaskViewModelTest {
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var mapper: Map<KType, NavType<TaskDTO?>>
    private lateinit var taskRepository: TaskRepository

    @Before
    fun setup() {
        savedStateHandle = mockk(relaxed = true)
        taskRepository = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun verify_state_is_empty_when_no_task_comes_from_route() {
        val savedStateHandle =
            SavedStateHandle.invoke(Routes.AddEditTaskScreen(null), taskDTONavTypeMapper)
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        assertEquals(
            AddEditTaskState(
                task = TaskDTO(),
                validationState = ValidationState(),
                isLoading = false,
                isEditMode = false
            ), viewModel.state.value
        )
    }

    @Test
    fun verify_state_has_task_comes_from_route() {
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )

        val savedStateHandle =
            SavedStateHandle.invoke(
                Routes.AddEditTaskScreen(expectedTask),
                taskDTONavTypeMapper
            )
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        assertEquals(
            AddEditTaskState(
                task = expectedTask,
                validationState = ValidationState(),
                isLoading = false,
                isEditMode = true
            ), viewModel.state.value
        )
    }

    @Test
    fun verify_save_function_adds_new_task_with_estimation() = runTest {
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )
        val savedStateHandle =
            SavedStateHandle.invoke(Routes.AddEditTaskScreen(null), taskDTONavTypeMapper)
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        viewModel.updateTaskState(expectedTask)
        viewModel.saveTask()
        coVerify { taskRepository.addTask(expectedTask) }
    }

    @Test
    fun verify_save_function_emits_navigate_back_signal() = runTest {
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )
        val savedStateHandle =
            SavedStateHandle.invoke(Routes.AddEditTaskScreen(null), taskDTONavTypeMapper)
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        val eventList = mutableListOf<AddEditTaskEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.toList(eventList)
        }
        viewModel.updateTaskState(expectedTask)
        viewModel.saveTask()
        assertEquals(AddEditTaskEvent.NavigateBack, eventList.last())

    }
    @Test
    fun verify_save_function_updates_existing_task()= runTest {
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )
        val savedStateHandle =
            SavedStateHandle.invoke(
                Routes.AddEditTaskScreen(expectedTask),
                taskDTONavTypeMapper
            )
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        viewModel.updateTaskState(expectedTask)
        viewModel.saveTask()
        coVerify { taskRepository.updateTask(expectedTask) }
    }
    @Test
    fun verify_save_function_updates_state_with_validation_errors()= runTest {
        val expectedTask = TaskDTO(
            id = 1,
            title = "",
            description = "",
            startDate = Clock.System.now().minus(1.days).toEpochMilliseconds(),
            endDate = Clock.System.now().minus(1.days).toEpochMilliseconds(),
            priority = 1
        )
        val savedStateHandle =
            SavedStateHandle.invoke(Routes.AddEditTaskScreen(null), taskDTONavTypeMapper)
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        viewModel.updateTaskState(expectedTask)
        viewModel.saveTask()
        assertEquals(
            ValidationState(
                title = "Title cannot be empty",
                description = "Description cannot be empty",
                startDate = "Start date cannot be before today.",
                endDate = "End date cannot be before today.",
                isFormValid = false
            ), viewModel.state.value.validationState
        )
    }
    @Test
    fun verify_delete_task_removes_task_from_db()= runTest {
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )

        val savedStateHandle =
            SavedStateHandle.invoke(
                Routes.AddEditTaskScreen(expectedTask),
                taskDTONavTypeMapper
            )
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        viewModel.deleteTask()
        coVerify { taskRepository.deleteTask(1) }
    }
    @Test
    fun verify_delete_task_emits_navigate_back_signal()= runTest {
        val eventList = mutableListOf<AddEditTaskEvent>()
        val expectedTask = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            startDate = 1620000000000,
            endDate = 1620000000000,
            priority = 1
        )

        val savedStateHandle =
            SavedStateHandle.invoke(
                Routes.AddEditTaskScreen(expectedTask),
                taskDTONavTypeMapper
            )
        val viewModel = AddEditTaskViewModelImpl(savedStateHandle, taskRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.toList(eventList)
        }
        viewModel.deleteTask()
        assertEquals(AddEditTaskEvent.NavigateBack, eventList.last())

    }
}