package com.ramazanm.devpomodoro.presentation

import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.PomodoroType
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import devpomodoro.composeapp.generated.resources.Res
import devpomodoro.composeapp.generated.resources.load_pomodoro_error
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration.Companion.seconds

@RunWith(RobolectricTestRunner::class)
class PomodoroViewModelTest() {
    private lateinit var pomodoroViewModel: PomodoroViewModel
    val repository: TaskRepository = mockk(relaxed = true)

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun verify_fetchCurrentPomodoro_updates_state_with_in_progress_task_details() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        assertEquals(
            testTaskList[0].task, pomodoroViewModel.pomodoroState.value.selectedTask
        )
    }

    @Test
    fun verify_fetchCurrentPomodoro_updates_state_with_next_not_started_pomodoro() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        assertEquals(
            testTaskList[0].pomodoros[1], pomodoroViewModel.pomodoroState.value.activePomodoro
        )
    }

    @Test
    fun verify_fetchCurrentPomodoro_updates_state_with_first_paused_pomodoro() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns
                TaskWithPomodorosDTO(
                    task = TaskDTO(
                        id = 1,
                        title = "Refactor Auth Module",
                        description = "Cleanup the LoginViewModel",
                        status = TaskStatus.STARTED,
                        source = TaskSourceType.LOCAL,
                        startDate = System.currentTimeMillis() - 3600000,
                        priority = 1
                    ), pomodoros = listOf(
                        PomodoroDTO(
                            101,
                            PomodoroType.WORK,
                            System.currentTimeMillis() - 3600000,
                            System.currentTimeMillis() - 2100000,
                            PomodoroStatus.PAUSED,
                            1
                        ), PomodoroDTO(
                            102,
                            PomodoroType.BREAK,
                            System.currentTimeMillis() - 2100000,
                            null,
                            PomodoroStatus.NOT_STARTED,
                            1
                        )
                    )
                )
        pomodoroViewModel.fetchCurrentPomodoro()
        assertEquals(
            PomodoroDTO(
                101,
                PomodoroType.WORK,
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis() - 2100000,
                PomodoroStatus.PAUSED,
                1
            ), pomodoroViewModel.pomodoroState.value.activePomodoro
        )
    }

    @Test
    fun verify_fetchCurrentPomodoro_updates_state_with_started_pomodoro() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns
                TaskWithPomodorosDTO(
                    task = TaskDTO(
                        id = 1,
                        title = "Refactor Auth Module",
                        description = "Cleanup the LoginViewModel",
                        status = TaskStatus.STARTED,
                        source = TaskSourceType.LOCAL,
                        startDate = System.currentTimeMillis() - 3600000,
                        priority = 1
                    ), pomodoros = listOf(
                        PomodoroDTO(
                            101,
                            PomodoroType.WORK,
                            System.currentTimeMillis() - 3600000,
                            System.currentTimeMillis() - 2100000,
                            PomodoroStatus.STARTED,
                            1
                        ), PomodoroDTO(
                            102,
                            PomodoroType.BREAK,
                            System.currentTimeMillis() - 2100000,
                            null,
                            PomodoroStatus.NOT_STARTED,
                            1
                        )
                    )
                )
        pomodoroViewModel.fetchCurrentPomodoro()
        assertEquals(
            PomodoroDTO(
                102,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 2100000,
                null,
                PomodoroStatus.NOT_STARTED,
                1
            ), pomodoroViewModel.pomodoroState.value.activePomodoro
        )
    }

    @Test
    fun verify_startPomodoro_updates_pomodoro_and_state() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]

        pomodoroViewModel.fetchCurrentPomodoro()
        pomodoroViewModel.startPomodoro()
        coVerify {
            repository.updatePomodoro(
                pomodoroViewModel.pomodoroState.value.activePomodoro.copy(
                    status = PomodoroStatus.STARTED
                )
            )
        }
        assertEquals(
            PomodoroDTO(
                102,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 2100000,
                System.currentTimeMillis(),
                PomodoroStatus.STARTED,
                1
            ), pomodoroViewModel.pomodoroState.value.activePomodoro

        )
    }

    @Test
    fun verify_pausePomodoro_updates_remainingSeconds_and_status_of_pomodoro() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        pomodoroViewModel.pausePomodoro(10.seconds)
        coVerify {
            repository.updatePomodoro(
                testTaskList[0].pomodoros[1].copy(
                    status = PomodoroStatus.PAUSED, remainingSeconds = 10.seconds.inWholeSeconds
                )
            )
        }
    }

    @Test
    fun verify_skipPomodoro_updates_status_of_pomodoro_to_not_needed() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        pomodoroViewModel.skipPomodoro()
        coVerify {
            repository.updatePomodoro(
                PomodoroDTO(
                    102,
                    PomodoroType.BREAK,
                    System.currentTimeMillis() - 2100000,
                    null,
                    PomodoroStatus.NOT_NEEDED,
                    1
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verify_skipPomodoro_emits_NextTask_event_when_last_pomodoro_finishes() = runTest {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        val eventList = mutableListOf<PomodoroEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            pomodoroViewModel.eventFlow.toList(eventList)
        }
        pomodoroViewModel.skipPomodoro()
        assertEquals(PomodoroEvent.NextTask, eventList.last())
    }

    @Test
    fun verify_getNextPomodoro_updates_pomodoro_to_interrupted() {
        coEvery { repository.getActiveTaskWithPomodoros() } returns testTaskList[0]
        pomodoroViewModel.fetchCurrentPomodoro()
        pomodoroViewModel.getNextPomodoro()
        coVerify {
            repository.updatePomodoro(
                PomodoroDTO(
                    102,
                    PomodoroType.BREAK,
                    System.currentTimeMillis() - 2100000,
                    null,
                    PomodoroStatus.INTERRUPTED,
                    1
                )
            )
        }
    }

    //Error Tests
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verify_ShowToast_event_emitted_when_getTaskWithPomodoros_returns_null() = runTest {
        coEvery { repository.getActiveTaskWithPomodoros() } returns null
        val eventList = mutableListOf<PomodoroEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            pomodoroViewModel.eventFlow.toList(eventList)
        }
        pomodoroViewModel.fetchCurrentPomodoro()
        assertEquals(PomodoroEvent.ShowToast(Res.string.load_pomodoro_error),eventList.last())
    }
}


val testTaskList = mutableListOf<TaskWithPomodorosDTO>(
    // Task 1: Single cycle, currently on a break
    TaskWithPomodorosDTO(
        task = TaskDTO(
            id = 1,
            title = "Refactor Auth Module",
            description = "Cleanup the LoginViewModel",
            status = TaskStatus.STARTED,
            source = TaskSourceType.LOCAL,
            startDate = System.currentTimeMillis() - 3600000,
            priority = 1
        ), pomodoros = listOf(
            PomodoroDTO(
                101,
                PomodoroType.WORK,
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis() - 2100000,
                PomodoroStatus.FINISHED,
                1
            ), PomodoroDTO(
                102,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 2100000,
                null,
                PomodoroStatus.NOT_STARTED,
                1
            )
        )
    ),

    // Task 2: Two full pomodoros completed, task finished
    TaskWithPomodorosDTO(
        task = TaskDTO(
            id = 2,
            title = "Weekly Newsletter",
            description = "Draft and schedule email",
            status = TaskStatus.FINISHED,
            source = TaskSourceType.LOCAL,
            startDate = System.currentTimeMillis() - 7200000,
            endDate = System.currentTimeMillis(),
            priority = 3
        ), pomodoros = listOf(
            // Couple 1
            PomodoroDTO(
                201,
                PomodoroType.WORK,
                System.currentTimeMillis() - 7200000,
                System.currentTimeMillis() - 5700000,
                PomodoroStatus.FINISHED,
                2
            ), PomodoroDTO(
                202,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 5700000,
                System.currentTimeMillis() - 5400000,
                PomodoroStatus.FINISHED,
                2
            ),
            // Couple 2
            PomodoroDTO(
                203,
                PomodoroType.WORK,
                System.currentTimeMillis() - 5400000,
                System.currentTimeMillis() - 3900000,
                PomodoroStatus.FINISHED,
                2
            ), PomodoroDTO(
                204,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 3900000,
                System.currentTimeMillis() - 3600000,
                PomodoroStatus.FINISHED,
                2
            )
        )
    ),

    // Task 3: High priority, 3 full pomodoros, not started a 4th work session
    TaskWithPomodorosDTO(
        task = TaskDTO(
            id = 3,
            title = "Fix Critical Bug #404",
            description = "App crashes on API 34",
            status = TaskStatus.PAUSED,
            source = TaskSourceType.LOCAL,
            startDate = System.currentTimeMillis() - 10800000,
            priority = 0
        ), pomodoros = listOf(
            // Couple 1
            PomodoroDTO(
                301,
                PomodoroType.WORK,
                System.currentTimeMillis() - 10800000,
                System.currentTimeMillis() - 9300000,
                PomodoroStatus.FINISHED,
                3
            ), PomodoroDTO(
                302,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 9300000,
                System.currentTimeMillis() - 9000000,
                PomodoroStatus.FINISHED,
                3
            ),
            // Couple 2
            PomodoroDTO(
                303,
                PomodoroType.WORK,
                System.currentTimeMillis() - 9000000,
                System.currentTimeMillis() - 7500000,
                PomodoroStatus.FINISHED,
                3
            ), PomodoroDTO(
                304,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 7500000,
                System.currentTimeMillis() - 7200000,
                PomodoroStatus.FINISHED,
                3
            ),
            // Couple 3
            PomodoroDTO(
                305,
                PomodoroType.WORK,
                System.currentTimeMillis() - 7200000,
                System.currentTimeMillis() - 5700000,
                PomodoroStatus.FINISHED,
                3
            ), PomodoroDTO(
                306,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 5700000,
                System.currentTimeMillis() - 5400000,
                PomodoroStatus.FINISHED,
                3
            ),
            // Starting next session
            PomodoroDTO(
                307,
                PomodoroType.WORK,
                System.currentTimeMillis() - 600000,
                null,
                PomodoroStatus.NOT_STARTED,
                3
            ), PomodoroDTO(
                308,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 600000,
                null,
                PomodoroStatus.NOT_STARTED,
                3
            )
        )
    ),

    // Task 4: Finished
    TaskWithPomodorosDTO(
        task = TaskDTO(
            id = 4,
            title = "Update Library Specs",
            description = "Synced from Jira",
            status = TaskStatus.STARTED,
            source = TaskSourceType.LOCAL,
            startDate = System.currentTimeMillis() - 4000000,
            priority = 2
        ), pomodoros = listOf(
            PomodoroDTO(
                401,
                PomodoroType.WORK,
                System.currentTimeMillis() - 4000000,
                System.currentTimeMillis() - 2500000,
                PomodoroStatus.FINISHED,
                4
            ), PomodoroDTO(
                402,
                PomodoroType.BREAK,
                System.currentTimeMillis() - 2500000,
                System.currentTimeMillis() - 2200000,
                PomodoroStatus.FINISHED,
                4
            )
        )
    )
)