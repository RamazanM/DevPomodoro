package com.ramazanm.devpomodoro.data

import com.ramazanm.devpomodoro.data.db.entity.TaskEntity
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskSourceType
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import kotlin.test.assertTrue

class TaskModelTest {
    @Test
    fun verifyTaskDTOHasInitialValues() {
        val taskDTO = TaskDTO()
        assertEquals(0L, taskDTO.id)
        assertEquals("", taskDTO.title)
        assertEquals("", taskDTO.description)
        assertEquals(TaskStatus.NOT_STARTED, taskDTO.status)
        assertEquals(TaskSourceType.LOCAL, taskDTO.source)
        assertEquals(0L, taskDTO.startDate)
        assertEquals(0L, taskDTO.endDate)
        assertEquals(0, taskDTO.priority)
    }

    @Test
    fun verifyTaskEntityHasInitialValues() {
        val date = kotlin.time.Clock.System.now().epochSeconds
        val taskEntity = TaskEntity()
        assertEquals(0L, taskEntity.id)
        assertEquals("", taskEntity.title)
        assertEquals("", taskEntity.description)
        assertEquals(TaskStatus.NOT_STARTED, taskEntity.status)
        assertEquals(TaskSourceType.LOCAL, taskEntity.source)
        assertTrue { date <= taskEntity.startDate + 5 && date >= taskEntity.startDate - 5 } //Since it could take some time to create the entity, we allow a 5 second margin of error.
        assertTrue {
            taskEntity.endDate?.let {
                date <= it + 5 && date >= it - 5
            } ?: false
            //Since it could take some time to create the entity, we allow a 5 second margin of error.
        }
        assertEquals(1, taskEntity.priority)
    }

    @Test
    fun verifyTaskDTOConversionToEntity() {
        val date: Long = Clock.systemUTC().millis()
        val endDate = date + 1000
        val taskDTO = TaskDTO(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            status = TaskStatus.NOT_STARTED,
            source = TaskSourceType.LOCAL,
            startDate = date,
            endDate = endDate,
            priority = 1
        )
        val taskEntity = taskDTO.toEntity()
        assertEquals(1L, taskEntity.id)
        assertEquals("Test Task", taskEntity.title)
        assertEquals("This is a test task", taskEntity.description)
        assertEquals(TaskStatus.NOT_STARTED, taskEntity.status)
        assertEquals(TaskSourceType.LOCAL, taskEntity.source)
        assertEquals(date, taskEntity.startDate)
        assertEquals(endDate, taskEntity.endDate)
        assertEquals(1, taskEntity.priority)
    }

    @Test
    fun verifyTaskEntityConversionToDTO() {
        val date: Long = Clock.systemUTC().millis()
        val endDate = date + 1000
        val taskEntity = TaskEntity(
            id = 1,
            title = "Test Task",
            description = "This is a test task",
            status = TaskStatus.NOT_STARTED,
            source = TaskSourceType.LOCAL,
            startDate = date,
            endDate = endDate,
            priority = 1
        )
        val taskDTO = taskEntity.toDTO()
        assertEquals(1L, taskDTO.id)
        assertEquals("Test Task", taskDTO.title)
        assertEquals("This is a test task", taskDTO.description)
        assertEquals(TaskStatus.NOT_STARTED, taskDTO.status)
        assertEquals(TaskSourceType.LOCAL, taskDTO.source)
        assertEquals(date, taskDTO.startDate)
        assertEquals(endDate, taskDTO.endDate)
        assertEquals(1, taskDTO.priority)
    }

}