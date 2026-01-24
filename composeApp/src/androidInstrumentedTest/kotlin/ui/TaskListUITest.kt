package com.ramazanm.devpomodoro.ui


import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ramazanm.devpomodoro.App
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import com.ramazanm.devpomodoro.presentation.TaskListViewModel
import com.ramazanm.devpomodoro.presentation.TaskListViewModelImpl
import com.ramazanm.devpomodoro.util.TestTags
import com.ramazanm.devpomodoro.util.TestTags.TASK_LIST_ITEM
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


@RunWith(AndroidJUnit4::class)
class TaskListUITest {
    val repository = mockk<TaskRepository>(relaxUnitFun = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun updateMockAndInitUI(mockBlock: () -> Unit) {
        mockBlock()
        val mockViewModel = TaskListViewModelImpl(repository)
        loadKoinModules(module {
            single<TaskListViewModel> { mockViewModel }
        })
        composeTestRule.setContent {
            App()
        }
    }

    @Test
    fun verify_my_tasks_title_bar_exists() {
        updateMockAndInitUI{}
        composeTestRule.onRoot().printToLog("1")
        composeTestRule.onNodeWithText("My Tasks").assertExists()
    }

    @Test
    fun verify_empty_list_message_shown_please_add_a_task_to_start() {
        updateMockAndInitUI {
            coEvery { repository.getTasksWithPomodoros() } returns emptyList()
        }

        composeTestRule.onNodeWithText("There is no Task. Please tap the + button to add a Task.")
            .assertExists()
    }

    @Test
    fun verify_non_empty_list_message_not_show_please_add_a_task_to_start() {
        //Arrange
        updateMockAndInitUI {
            coEvery { repository.getTasksWithPomodoros() } returns listOf(
                TaskWithPomodorosDTO(TaskDTO(1), listOf()),
                TaskWithPomodorosDTO(TaskDTO(2), listOf()),
                TaskWithPomodorosDTO(TaskDTO(3), listOf()),
                TaskWithPomodorosDTO(TaskDTO(4), listOf()),
            )
        }
        //Assert
        composeTestRule.onNodeWithText("There is no Task. Please tap the + button to add a Task.")
            .assertDoesNotExist()
    }

    @Test
    fun verify_correct_amount_of_tasks_shown() {
        //Arrange
        updateMockAndInitUI {
            coEvery { repository.getTasksWithPomodoros() } returns listOf(
                TaskWithPomodorosDTO(TaskDTO(1), listOf()),
                TaskWithPomodorosDTO(TaskDTO(2), listOf()),
                TaskWithPomodorosDTO(TaskDTO(3), listOf()),
                TaskWithPomodorosDTO(TaskDTO(4), listOf()),
            )
        }
        //Assert
        composeTestRule.onAllNodesWithTag(TASK_LIST_ITEM).assertCountEquals(4)
    }

    @Test
    fun verify_task_list_item_content_is_correct() {
        //Arrange
        updateMockAndInitUI {
            coEvery { repository.getTasksWithPomodoros() } returns listOf(
                TaskWithPomodorosDTO(
                    TaskDTO(
                        1, title = "Test Task Title", description = "Test Task Description"
                    ), listOf()
                ),
            )
        }
        //Assert
        composeTestRule.onNodeWithTag("TaskTitle").assertTextEquals("Test Task Title")
        composeTestRule.onNodeWithTag("TaskDescription").assertTextEquals("Test Task Description")
    }

    @Test
    fun verify_add_task_fab_is_visible_on_task_list() {
        //Arrange
        updateMockAndInitUI {  }
        //Assert
        composeTestRule.onNodeWithTag(TestTags.ADD_TASK_FAB).assertExists()
    }
    @Test
    fun verify_add_task_fab_is_invisible_on_other_pages() {
        //Arrange
        updateMockAndInitUI {  }
        //Act
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_NAVBAR_ITEM).performClick()
        //Assert
        composeTestRule.onNodeWithTag(TestTags.ADD_TASK_FAB).assertDoesNotExist()

    }

    @Test
    fun verify_started_task_shows_pomodoro_progress() {
    }

    @Test
    fun verify_started_task_shows_countdown() {
    }

    @Test
    fun verify_started_task_shows_pause_and_skip_btn() {
    }

    @Test
    fun verify_paused_task_shows_start_button() {
    }

    @Test
    fun verify_swipe_left_shows_edit_and_delete_button() {
    }

    @Test
    fun verify_add_task_fab_redirects_to_add_edit_screen() {
    }

    @Test
    fun verify_edit_button_redirects_to_add_edit_screen() {
    }

    @Test
    fun verify_delete_button_shows_confirmation_dialog() {
    }

    @Test
    fun verify_delete_confirmation_dialog_has_description_and_two_buttons() {
    }

    @Test
    fun verify_confirm_delete_button_calls_delete_function() {
    }
}