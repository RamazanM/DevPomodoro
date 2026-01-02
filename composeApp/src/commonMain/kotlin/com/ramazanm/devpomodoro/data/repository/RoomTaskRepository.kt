package com.ramazanm.devpomodoro.data.repository

import com.ramazanm.devpomodoro.data.db.dao.PomodoroDAO
import com.ramazanm.devpomodoro.data.db.dao.TaskDAO
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.PomodoroStatus
import com.ramazanm.devpomodoro.data.dto.PomodoroType
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskStatus
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO

class RoomTaskRepository(private val taskDAO: TaskDAO, private val pomodoroDAO: PomodoroDAO) :
    TaskRepository() {
    override suspend fun getTasksWithPomodoros(): List<TaskWithPomodorosDTO> {
        return taskDAO.getAllTasksWithPomodoros().map { it.toDTO() }
    }

    override suspend fun getTaskWithPomodoros(taskId: Long): TaskWithPomodorosDTO {
        return taskDAO.getTaskWithPomodoros(taskId).toDTO()
    }

    override suspend fun increaseEstimation(taskDTO: TaskDTO) {
        if (taskDTO.status == TaskStatus.FINISHED)
            updateTask(taskDTO.copy(status = TaskStatus.PAUSED))
        addPomodoro(
            PomodoroDTO(
                type = PomodoroType.WORK,
                status = if (taskDTO.status == TaskStatus.FINISHED) PomodoroStatus.FINISHED else PomodoroStatus.NOT_STARTED,
                taskId = taskDTO.id
            )
        )
        addPomodoro(
            PomodoroDTO(
                type = PomodoroType.BREAK,
                status = if (taskDTO.status == TaskStatus.FINISHED) PomodoroStatus.FINISHED else PomodoroStatus.NOT_STARTED,
                taskId = taskDTO.id
            )
        )
    }

    override suspend fun decreaseEstimation(taskDTO: TaskDTO) {
        val taskWithPomodoros = taskDTO.id?.let { getTaskWithPomodoros(it) }
        val notStartedPomodoros =
            taskWithPomodoros?.pomodoros?.filter { it.status == PomodoroStatus.NOT_STARTED }
                ?.takeLast(2)
        if((notStartedPomodoros?.size ?: 0) < 2) throw NoSuchElementException("There is no available task to remove.")
        notStartedPomodoros?.forEach { it.id?.let { id -> pomodoroDAO.deleteById(id) } }
    }

    override suspend fun getActiveTaskWithPomodoros(): TaskWithPomodorosDTO? {
        return taskDAO.getActiveTask()?.toDTO()
    }

    override suspend fun addTaskWithEstimation(
        taskDTO: TaskDTO, estimation: Int
    ) {
        val taskId = addTask(taskDTO)
        (0..<estimation).forEach { _ ->
            addPomodoro(
                PomodoroDTO(
                    type = PomodoroType.WORK,
                    status = if (taskDTO.status == TaskStatus.FINISHED) PomodoroStatus.FINISHED else PomodoroStatus.NOT_STARTED,
                    taskId = taskId
                )
            )
            addPomodoro(
                PomodoroDTO(
                    type = PomodoroType.BREAK,
                    status = if (taskDTO.status == TaskStatus.FINISHED) PomodoroStatus.FINISHED else PomodoroStatus.NOT_STARTED,
                    taskId = taskId
                )
            )
        }
    }

    override suspend fun completeTask(taskDTO: TaskDTO) {
        val taskWithPomodoros = taskDTO.id?.let { getTaskWithPomodoros(it) }
        taskWithPomodoros?.task?.copy(status = TaskStatus.FINISHED)?.let { updateTask(it) }
        taskWithPomodoros?.pomodoros?.forEach {
            if (it.status != PomodoroStatus.FINISHED) {
                cancelPomodoro(it)
            }
        }
    }

    override suspend fun completePomodoro(pomodoroDTO: PomodoroDTO) {
        updatePomodoro(pomodoroDTO.copy(status = PomodoroStatus.FINISHED))
    }

    override suspend fun cancelPomodoro(pomodoroDTO: PomodoroDTO) {
        updatePomodoro(pomodoroDTO.copy(status = PomodoroStatus.NOT_NEEDED))
    }

    override suspend fun setActiveTask(taskDTO: TaskDTO) {
        //Get previous active task
        val activeTask = getActiveTaskWithPomodoros()
        val activePomodoro = activeTask?.pomodoros?.find { it.status == PomodoroStatus.STARTED }
        // Pause active task and pomodoro
        activePomodoro?.copy(status = PomodoroStatus.INTERRUPTED)?.let { updatePomodoro(it) }
        activeTask?.task?.copy(status = TaskStatus.PAUSED)?.let { updateTask(it) }
        // Set new task as active
        updateTask(taskDTO.copy(status = TaskStatus.STARTED))
    }

    override suspend fun addPomodoro(
        pomodoroDTO: PomodoroDTO
    ) {
        pomodoroDAO.insert(pomodoroDTO.toEntity())
    }

    override suspend fun updatePomodoro(pomodoroDTO: PomodoroDTO) {
        pomodoroDAO.update(pomodoroDTO.toEntity())
    }

    override suspend fun getTask(id: Long): TaskDTO? {
        return taskDAO.getById(id)?.toDTO()
    }

    override suspend fun addTask(taskDTO: TaskDTO): Long {
        return taskDAO.insert(taskDTO.toEntity())
    }

    override suspend fun updateTask(taskDTO: TaskDTO) {
        if (taskDTO.id != null && taskDAO.getById(taskDTO.id) == null) {
            throw IndexOutOfBoundsException("Task with ID ${taskDTO.id} not found")

        }
        taskDAO.update(taskDTO.toEntity())
    }

    override suspend fun deleteTask(id: Long) {
        taskDAO.deleteById(id)
    }
}