package com.ramazanm.devpomodoro.data.dto

import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class TaskDTOTest {

    @Test
    fun `all fields has default values`() {
        val taskDTO = TaskDTO()
        assertEquals(taskDTO.id, null)
        assertEquals(taskDTO.title, "")
        assertEquals(taskDTO.description, "")
        assertEquals(taskDTO.status, "")
        assertEquals(taskDTO.source, TaskSourceType.LOCAL)
        assertEquals(taskDTO.startDate, 0L)
        assertEquals(taskDTO.endDate, 0L)
        assertEquals(taskDTO.priority, 0)
    }

    @Test
    fun toEntity_withAllFieldsPopulated_mapsAllFieldsCorrectly() {
        // Check if TaskEntity is created with all fields correctly mapped from TaskDTO when all fields in TaskDTO are populated.

        // --- 1. Arrange: Set up test data predictably ---
        val startTime = Clock.System.now()
        val endTime = startTime.plus(1.days)

        val taskDTO = TaskDTO(
            id = 1,
            title = "Title",
            description = "Description",
            status = "TODO",
            source = TaskSourceType.LOCAL,
            startDate = startTime.epochSeconds,
            endDate = endTime.epochSeconds,
            priority = 1
        )

        // --- 2. Act: Call the function under test ---
        val actualEntity = taskDTO.toEntity()

        // --- 3. Assert: Verify the result ---
        // Create the expected result object
        val expectedEntity = TaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            status = "TODO",
            source = TaskSourceType.LOCAL,
            startDate = startTime.epochSeconds,
            endDate = endTime.epochSeconds,
            priority = 1
        )

        // A single, clear assertion. This works because data classes
        // provide a correct `equals` implementation out of the box.
        assertEquals(expectedEntity, actualEntity)
    }
}

